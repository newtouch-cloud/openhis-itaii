package com.openhis.web.outpatientmanage.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.exception.ServiceException;
import com.core.common.utils.*;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.outpatientmanage.appservice.IOutpatientInfusionAppService;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionInitDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
import com.openhis.web.outpatientmanage.mapper.OutpatientInfusionAppMapper;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.mapper.ServiceRequestMapper;
import com.openhis.workflow.service.IServiceRequestService;

/**
 * 门诊管理——输液实现类
 *
 * @author liuhr
 * @date 2025/3/12
 */
@Service
@Transactional
public class OutpatientInfusionAppServiceImpl implements IOutpatientInfusionAppService {

    @Resource
    private OutpatientInfusionAppMapper outpatientInfusionAppMapper;

    @Autowired
    private IServiceRequestService serviceRequestService;

    @Autowired
    private ServiceRequestMapper serviceRequestMapper;

    @Autowired
    private AssignSeqUtil assignSeqUtil;

    /**
     * 门诊输液初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {
        OutpatientInfusionInitDto initDto = new OutpatientInfusionInitDto();
        // 执行状态
        List<OutpatientInfusionInitDto.ServiceStatus> serviceStatusOptions = new ArrayList<>();
        serviceStatusOptions.add(new OutpatientInfusionInitDto.ServiceStatus(RequestStatus.COMPLETED.getValue(),
            RequestStatus.COMPLETED.getInfo()));
        serviceStatusOptions.add(new OutpatientInfusionInitDto.ServiceStatus(RequestStatus.IN_PROGRESS.getValue(),
            RequestStatus.IN_PROGRESS.getInfo()));
        serviceStatusOptions.add(new OutpatientInfusionInitDto.ServiceStatus(RequestStatus.CANCELLED.getValue(),
            RequestStatus.CANCELLED.getInfo()));
        initDto.setServiceStatusOptions(serviceStatusOptions);
        return R.ok(initDto);
    }

    /**
     * 获取门诊输液记录的患者列表
     *
     * @param outpatientInfusionPatientDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 分页查询
     */
    @Override
    public R<?> getOutpatientInfusionPatientList(OutpatientInfusionPatientDto outpatientInfusionPatientDto,
        String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<OutpatientInfusionPatientDto> queryWrapper =
            HisQueryUtils.buildQueryWrapper(outpatientInfusionPatientDto, searchKey,
                new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientBusNo,
                    CommonConstants.FieldName.EncounterBusNo, CommonConstants.FieldName.PatientName,
                    CommonConstants.FieldName.PatientPyStr, CommonConstants.FieldName.PatientWbStr)),
                request);
        // 查询输液患者列表
        IPage<OutpatientInfusionPatientDto> outpatientInfusionPatientPage =
            outpatientInfusionAppMapper.getOutpatientInfusionPatient(new Page<>(pageNo, pageSize), queryWrapper,
                RequestStatus.IN_PROGRESS.getValue(), RequestStatus.COMPLETED.getValue(),
                RequestStatus.CANCELLED.getValue(), CommonConstants.TableName.MED_MEDICATION_REQUEST);
        outpatientInfusionPatientPage.getRecords().forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 性别
            e.setServiceStatus_enumText(EnumUtils.getInfoByValue(RequestStatus.class, e.getServiceStatus()));
            if (e.getBirthDate() != null) {
                // 计算年龄
                e.setAgeString(AgeCalculatorUtil.getAge(e.getBirthDate()));
            }
        });
        return R.ok(outpatientInfusionPatientPage);
    }

    /**
     * 门诊输液执行记录查询
     *
     * @param serviceReqId 诊疗项目id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 门诊输液执行历史记录列表
     */
    @Override
    public R<?> getInfusionPerformRecord(Long serviceReqId, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<OutpatientInfusionRecordDto> queryWrapper =
            HisQueryUtils.buildQueryWrapper(null, null, null, null);
        // 查询门诊输液执行记录
        Page<OutpatientInfusionRecordDto> outpatientInfusionRecordPage =
            outpatientInfusionAppMapper.selectInfusionPerformRecord(new Page<>(pageNo, pageSize), queryWrapper,
                serviceReqId, RequestStatus.COMPLETED.getValue());
        outpatientInfusionRecordPage.getRecords().forEach(e -> {
            // 诊疗状态
            e.setServiceStatus_enumText(EnumUtils.getInfoByValue(RequestStatus.class, e.getServiceStatus()));
        });
        return R.ok(outpatientInfusionRecordPage);

    }

    /**
     * 门诊输液待执行记录查询
     *
     * @param encounterId 就诊ID
     * @param serviceStatus 就诊状态
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 门诊输液待执行记录列表
     */
    @Override
    public R<?> getInfusionPendingRecord(Long encounterId, Integer serviceStatus, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<OutpatientInfusionRecordDto> queryWrapper = HisQueryUtils
            .buildQueryWrapper(new OutpatientInfusionRecordDto().setServiceStatus(serviceStatus), null, null, null);
        // 查询门诊输液待执行记录
        Page<OutpatientInfusionRecordDto> outpatientInfusionRecordPage =
            outpatientInfusionAppMapper.selectInfusionPendingRecord(new Page<>(pageNo, pageSize), queryWrapper,
                encounterId, RequestStatus.COMPLETED.getValue(), CommonConstants.TableName.MED_MEDICATION_REQUEST);
        outpatientInfusionRecordPage.getRecords().forEach(e -> {
            // 是否皮试
            e.setSkinTestFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getSkinTestFlag()));
            // 发药状态
            e.setDispenseStatus_enumText(EnumUtils.getInfoByValue(DispenseStatus.class, e.getDispenseStatus()));
            // 诊疗状态
            e.setServiceStatus_enumText(EnumUtils.getInfoByValue(RequestStatus.class, e.getServiceStatus()));
        });
        return R.ok(outpatientInfusionRecordPage);
    }

    /**
     * 输液执行
     *
     * @param serviceReqIdList 输液请求id列表
     * @return 执行结果
     */
    @Override
    public R<?> infusionPerform(List<Long> serviceReqIdList) {
        // todo:查出对应的耗材并发放

        // 获取执行人，执行科室，当前时间
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        Long orgId = SecurityUtils.getLoginUser().getOrgId();
        Date now = DateUtils.getNowDate();
        List<ServiceRequest> serviceRequestList =
            serviceRequestService.listByIds(serviceReqIdList.stream().distinct().collect(Collectors.toList()));
        // 新增一条执行记录
        for (ServiceRequest serviceRequest : serviceRequestList) {
            serviceRequest.setBasedOnId(serviceRequest.getId()).setStatusEnum(RequestStatus.COMPLETED.getValue())
                .setOccurrenceEndTime(now).setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10))
                .setBasedOnTable(CommonConstants.TableName.WOR_SERVICE_REQUEST).setPerformerId(practitionerId)
                .setOrgId(orgId).setGroupId(null).setId(null);
            serviceRequestService.save(serviceRequest);
        }
        // 获取诊疗执行信息
        List<OutpatientInfusionRecordDto> outpatientInfusionRecordList =
            outpatientInfusionAppMapper.selectPerformInfo(serviceReqIdList, RequestStatus.COMPLETED.getValue());
        if (!outpatientInfusionRecordList.isEmpty()) {
            for (OutpatientInfusionRecordDto outpatientInfusionRecord : outpatientInfusionRecordList) {
                // 校验是否已发药
                if (!DispenseStatus.COMPLETED.getValue().equals(outpatientInfusionRecord.getDispenseStatus())) {
                    throw new ServiceException("请等待发药后再执行");
                }
                // 校验是否已经执行
                if (outpatientInfusionRecord.getExecuteNum().equals(outpatientInfusionRecord.getPerformCount())) {
                    // 更新主诊疗执行状态
                    serviceRequestMapper.update(null,
                        new LambdaUpdateWrapper<ServiceRequest>()
                            .eq(ServiceRequest::getId, outpatientInfusionRecord.getServiceId())
                            .set(ServiceRequest::getStatusEnum, RequestStatus.COMPLETED.getValue())
                            .set(ServiceRequest::getOccurrenceEndTime, now));

                } else if (outpatientInfusionRecord.getExecuteNum() < (outpatientInfusionRecord.getPerformCount())) {
                    throw new ServiceException("当前项目已全部执行，请勿重复执行");
                }
            }
        }
        return R.ok("执行成功");
    }

    /**
     * 修改输液执行时间
     *
     * @param serviceReqId 患者输液信息
     * @param performTime 患者输液信息
     * @return 执行结果
     */
    @Override
    public R<?> editPatientInfusionTime(Long serviceReqId, String performTime) {
        // 更新输液执行时间
        int countUpdate = serviceRequestMapper.update(null,
            new LambdaUpdateWrapper<ServiceRequest>().eq(ServiceRequest::getId, serviceReqId)
                .set(ServiceRequest::getOccurrenceEndTime, DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", performTime)));
        if (countUpdate >= 0) {
            return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"执行时间"}));
        }
        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 撤销执行
     *
     * @param serviceReqId 输液请求id
     * @return 撤销结果
     */
    @Override
    public R<?> cancelInfusionPerform(Long serviceReqId) {
        // todo:查出对应的耗材并退回到药房

        // 获取执行人，执行科室，当前时间
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        Long orgId = SecurityUtils.getLoginUser().getOrgId();
        Date now = DateUtils.getNowDate();
        // 获取诊疗项目信息
        ServiceRequest serviceRequest = serviceRequestService.getById(serviceReqId);
        if (serviceRequest != null) {
            // 重复取消校验
            if (RequestStatus.CANCELLED.getValue().equals(serviceRequest.getStatusEnum())) {
                return R.fail("已取消执行，请勿重复操作");
            }
            boolean result = serviceRequestService.updateCancelledStatus(serviceReqId, now, practitionerId, orgId);
            // 更新主服务请求状态为待执行
            serviceRequestService.updatePreparationStatus(List.of(serviceRequest.getBasedOnId()));
            if (result) {
                // 判断是否全部取消执行
                boolean exists = serviceRequestMapper.exists(new LambdaQueryWrapper<ServiceRequest>()
                    .eq(ServiceRequest::getBasedOnId, serviceRequest.getBasedOnId())
                    .eq(ServiceRequest::getStatusEnum, RequestStatus.COMPLETED.getValue()));
                if (!exists) {
                    // 全部取消执行后更新主服务请求状态为已取消
                    serviceRequestService.updateCancelledStatus(serviceRequest.getBasedOnId(), null, null, null);
                }
            }
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        return R.ok("取消执行成功");
    }
}
