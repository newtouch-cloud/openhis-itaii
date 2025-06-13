package com.openhis.workflow.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.model.LoginUser;
import com.openhis.administration.domain.Practitioner;
import com.openhis.common.enums.RequestStatus;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.domain.SupplyDelivery;
import com.openhis.workflow.domain.SupplyRequest;

/**
 * 服务申请管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IServiceRequestService extends IService<ServiceRequest> {

    /**
     * 查询服务申请管理中basedOnId相同的个数
     *
     * @param basedOnId 请求基于什么的ID
     * @return basedOnId相同的个数
     */
    Long countServiceRequestByBasedOnId(Long basedOnId);

    /**
     * 更新执行诊疗状态：停止
     *
     * @param serReqIdList 服务请求id列表
     */
    void updateStopRequestStatus(List<Long> serReqIdList);

    /**
     * 获取执行过的诊疗数据
     *
     * @param basedOnId 请求基于什么的ID
     */
    List<ServiceRequest> selectServiceRequestByBasedOnId(Long basedOnId);

    /**
     * 执行诊疗
     *
     * @param serviceRequest 服务申请信息
     * @param now 当前时间
     * @param loginUser 登录用户信息
     * @param step 执行次数
     */
    ServiceRequest createCompletedServiceRequest(ServiceRequest serviceRequest, Date now, Practitioner loginUser,
        String step);

    /**
     * 服务申请状态：已完成
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
    boolean completedStatusEnum(Long id, Date now, Practitioner loginUser);

    /**
     * 更新服务申请状态：取消
     *
     * @param serviceReqId 服务请求id
     * @param now 更新时间
     * @param practitionerId 执行人
     * @param orgId 执行科室
     * @return 更新结果
     */
    boolean updateCancelledStatus(Long serviceReqId, Date now, Long practitionerId,Long orgId);

    /**
     * 服务申请状态：已发送
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
     boolean activeStatusEnum(Long id, Date now, Practitioner loginUser);

    /**
     * 更新服务状态：待执行
     *
     * @param serviceRequestIdList 请求id列表
     */
    void updatePreparationStatus(List<Long> serviceRequestIdList);

    /**
     * 更新服务状态：待发送
     *
     * @param serReqIdList 请求id列表
     */
    void updateDraftStatusBatch(List<Long> serReqIdList);

    /**
     * 更新服务申请里的打印次数字段
     *
     * @param id 服务申请id
     * @param num 打印次数
     * @return 更新结果
     */
    void updateCountPint(Long id,Integer num);

}