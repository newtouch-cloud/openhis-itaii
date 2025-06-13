package com.openhis.workflow.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Practitioner;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.RequestStatus;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.mapper.ServiceRequestMapper;
import com.openhis.workflow.service.IServiceRequestService;

/**
 * 服务申请管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ServiceRequestServiceImpl extends ServiceImpl<ServiceRequestMapper, ServiceRequest>
    implements IServiceRequestService {

    /**
     * 查询服务申请管理中basedOnId相同的个数
     *
     * @param basedOnId 请求基于什么的ID
     * @return basedOnId相同的个数
     */
    @Override
    public Long countServiceRequestByBasedOnId(Long basedOnId) {
        LambdaQueryWrapper<ServiceRequest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServiceRequest::getBasedOnId, basedOnId);
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 更新执行诊疗状态：停止
     *
     * @param serReqIdList 服务请求id列表
     */
    @Override
    public void updateStopRequestStatus(List<Long> serReqIdList) {
        baseMapper.update(new ServiceRequest().setStatusEnum(RequestStatus.STOPPED.getValue()),
            new LambdaUpdateWrapper<ServiceRequest>().in(ServiceRequest::getId, serReqIdList));
    }

    /**
     * 获取执行过的诊疗数据
     *
     * @param basedOnId 请求基于什么的ID
     */
    @Override
    public List<ServiceRequest> selectServiceRequestByBasedOnId(Long basedOnId) {
        return (baseMapper
            .selectList(new LambdaQueryWrapper<ServiceRequest>().eq(ServiceRequest::getBasedOnId, basedOnId)
                .eq(ServiceRequest::getStatusEnum, RequestStatus.COMPLETED.getValue())));
    }

    /**
     * 执行诊疗
     *
     * @param serviceRequest 服务申请信息
     * @param now 当前时间
     * @param loginUser 登录用户信息
     * @param step 执行次数
     */
    @Override
    public ServiceRequest createCompletedServiceRequest(ServiceRequest serviceRequest, Date now, Practitioner loginUser,
        String step) {
        // 服务请求编码
        serviceRequest.setBusNo(serviceRequest.getBusNo() + "." + step);
        // 请求基于什么
        serviceRequest.setBasedOnTable(CommonConstants.TableName.WOR_SERVICE_REQUEST);
        // 请求基于什么的ID
        serviceRequest.setBasedOnId(serviceRequest.getId());
        // 数量
        serviceRequest.setQuantity(1);
        // 状态
        serviceRequest.setStatusEnum(RequestStatus.COMPLETED.getValue());
        // 执行人
        serviceRequest.setPerformerId(loginUser.getId());
        // 执行位置
        serviceRequest.setLocationId(loginUser.getOrgId());
        // 预计执行时间
        serviceRequest.setOccurrenceStartTime(now);
        // id
        serviceRequest.setId(null);
        // 新增服务申请
        baseMapper.insert(serviceRequest);

        return serviceRequest;
    }

    /**
     * 服务申请状态：已完成
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
    @Override
    public boolean completedStatusEnum(Long id, Date now, Practitioner loginUser) {
        int updateCount = baseMapper.update(null, new LambdaUpdateWrapper<ServiceRequest>()
            .eq(ServiceRequest::getId, id).set(ServiceRequest::getStatusEnum, RequestStatus.COMPLETED.getValue())
            .set(ServiceRequest::getPerformerId, loginUser.getId())
            .set(ServiceRequest::getLocationId, loginUser.getOrgId()).set(ServiceRequest::getOccurrenceStartTime, now));
        return updateCount > 0;
    }

    /**
     * 更新服务申请状态：取消
     *
     * @param serviceReqId 服务请求id
     * @param now 更新时间
     * @param practitionerId 执行人
     * @param orgId 执行科室
     * @return 更新结果
     */
    @Override
    public boolean updateCancelledStatus(Long serviceReqId, Date now, Long practitionerId, Long orgId) {
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<ServiceRequest>().eq(ServiceRequest::getId, serviceReqId)
                .set(ServiceRequest::getStatusEnum, RequestStatus.CANCELLED.getValue())
                .set(ServiceRequest::getPerformerId, practitionerId).set(ServiceRequest::getOrgId, orgId)
                .set(ServiceRequest::getOccurrenceStartTime, now));
        return updateCount > 0;
    }

    /**
     * 服务申请状态：已发送
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
    @Override
    public boolean activeStatusEnum(Long id, Date now, Practitioner loginUser) {
        int updateCount = baseMapper.update(null, new LambdaUpdateWrapper<ServiceRequest>()
            .eq(ServiceRequest::getId, id).set(ServiceRequest::getStatusEnum, RequestStatus.ACTIVE.getValue())
            .set(ServiceRequest::getPerformerId, loginUser.getId())
            .set(ServiceRequest::getLocationId, loginUser.getOrgId()).set(ServiceRequest::getOccurrenceStartTime, now));
        return updateCount > 0;
    }

    /**
     * 更新服务状态：待执行
     *
     * @param serviceRequestIdList 请求id列表
     */
    @Override
    public void updatePreparationStatus(List<Long> serviceRequestIdList) {
        baseMapper.update(null,
            new LambdaUpdateWrapper<ServiceRequest>()
                .set(ServiceRequest::getStatusEnum, RequestStatus.IN_PROGRESS.getValue())
                .in(ServiceRequest::getId, serviceRequestIdList));
    }

    /**
     * 更新服务状态：待发送
     *
     * @param serReqIdList 请求id列表
     */
    @Override
    public void updateDraftStatusBatch(List<Long> serReqIdList) {
        baseMapper.update(null,
            new LambdaUpdateWrapper<ServiceRequest>().set(ServiceRequest::getStatusEnum, RequestStatus.DRAFT.getValue())
                .in(ServiceRequest::getId, serReqIdList));
    }

    /**
     * 更新服务申请里的打印次数字段
     *
     * @param id 服务申请id
     * @param num 打印次数
     * @return 更新结果
     */
    @Override
    public void updateCountPint(Long id, Integer num) {
        // 根据 id 查询实体
        ServiceRequest serviceRequest = baseMapper.selectById(id);
        if (serviceRequest != null) {
            // 获取当前的 countPrint 值，如果为 null，默认为 0L
            int currentPrintCount = serviceRequest.getPrintCount();
            // 对 countPrint 字段进行加法操作
            int newPrintCount = currentPrintCount + num;
            // 更新实体
            serviceRequest.setPrintCount(newPrintCount);
            baseMapper.updateById(serviceRequest);
        }
    }

}