package com.openhis.web.outpatientmanage.appservice.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.service.IPractitionerRoleService;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.enums.ClinicalStatus;
import com.openhis.common.enums.EventStatus;
import com.openhis.common.enums.Whether;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.outpatientmanage.appservice.IOutpatientInfusionRecordService;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionInitDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionSearchParam;
import com.openhis.web.outpatientmanage.mapper.OutpatientManageMapper;
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
public class OutpatientInfusionRecordServiceImpl implements IOutpatientInfusionRecordService {

    @Resource
    OutpatientManageMapper outpatientManageMapper;

    @Autowired
    IServiceRequestService serviceRequestService;

    @Autowired
    IPractitionerService practitionerService;

    @Autowired
    IPractitionerRoleService practitionerRoleService;

    @Autowired
    ServiceRequestMapper serviceRequestMapper;

    /**
     * 获取门诊输液记录初期数据列表
     *
     * @return 门诊输液记录初期数据列表
     */
    @Override
    public OutpatientInfusionInitDto getOutpatientInfusionInit() {
        OutpatientInfusionInitDto initDto = new OutpatientInfusionInitDto();

        // 获取皮试结果
        List<OutpatientInfusionInitDto.statusEnumOption> statusEnumOptions2 = Stream.of(ClinicalStatus.values())
            .map(status -> new OutpatientInfusionInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setClinicalStatus(statusEnumOptions2);

        // 获取当天日期
        LocalDateTime beginTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), true);
        LocalDateTime endTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), false);
        // 创建查询包装器
        LambdaQueryWrapper<OutpatientInfusionRecordDto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(OutpatientInfusionRecordDto::getOccurrenceStartTime, beginTime);
        queryWrapper.le(OutpatientInfusionRecordDto::getOccurrenceEndTime, endTime);

        // 从数据库获取输液记录列表
        List<OutpatientInfusionRecordDto> infusionList =
            outpatientManageMapper.getOutpatientInfusionRecord(queryWrapper);
        // 遍历列表并处理每个记录
        infusionList.forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 药品状态
            e.setClinicalStatusEnum_enumText(EnumUtils.getInfoByValue(EventStatus.class, e.getMedicationStatusEnum()));
            // 皮试标志
            e.setSkinTestFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getSkinTestFlag()));
            // 只有皮试药品才显示皮试结果
            if (e.getSkinTestFlag() == Whether.YES.getValue()) {
                // 皮试结果
                e.setMedicationStatusEnum_enumText(
                    EnumUtils.getInfoByValue(ClinicalStatus.class, e.getClinicalStatusEnum()));
            }
        });

        initDto.setInfusionList(infusionList);

        return initDto;
    }

    /**
     * 获取门诊输液记录的患者列表
     *
     * @param outpatientInfusionSearchParam 门诊输液记录的患者列表查询参数
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 分页查询
     */
    @Override
    public IPage<OutpatientInfusionPatientDto> getOutpatientInfusionPatient(
        OutpatientInfusionSearchParam outpatientInfusionSearchParam, Integer pageNo, Integer pageSize) {

        LocalDateTime beginTime;
        LocalDateTime endTime;
        String searchKey;
        // 搜索key为空
        if (outpatientInfusionSearchParam == null) {
            searchKey = null;
        } else {
            searchKey = outpatientInfusionSearchParam.getSearchKey();
        }
        // 任意开始结束时间为空，默认查询当天日期记录
        if (outpatientInfusionSearchParam.getBeginTime() == null
            || outpatientInfusionSearchParam.getEndTime() == null) {
            beginTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), true);
            endTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), false);
        } else {// 时间不空，删选时间
            beginTime = DateUtils.startDayOrEndDay(outpatientInfusionSearchParam.getBeginTime(), true);
            endTime = DateUtils.startDayOrEndDay(outpatientInfusionSearchParam.getEndTime(), false);
        }

        // 构建查询条件
        QueryWrapper<OutpatientInfusionPatientDto> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientBusNo,
                CommonConstants.FieldName.EncounterBusNo, CommonConstants.FieldName.PatientName)),
            null);
        // based_on_id 是为空的
        queryWrapper.eq("based_on_id", null);
        // 状态是未完成的
        queryWrapper.in("service_status", EventStatus.IN_PROGRESS.getValue(), EventStatus.NOT_DONE.getValue());
        // 添加时间段查询条件
        if (beginTime != null && endTime != null) {
            queryWrapper.ge("create_time", beginTime);
            queryWrapper.le("create_time", endTime);
        }

        IPage<OutpatientInfusionPatientDto> outpatientInfusionPatientDto =
            outpatientManageMapper.getOutpatientInfusionPatient(new Page<>(pageNo, pageSize), queryWrapper);

        outpatientInfusionPatientDto.getRecords().forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAgeString(AgeCalculatorUtil.getAge(e.getBirthDate()));
        });

        return outpatientInfusionPatientDto;
    }

    /**
     * 查询单个患者门诊输液记录查询
     *
     * @param outpatientInfusionPatientDto 患者输液信息
     * @return 门诊输液记录列表
     */
    @Override
    public List<OutpatientInfusionRecordDto>
        getPatientInfusionRecord(OutpatientInfusionPatientDto outpatientInfusionPatientDto) {

        if (outpatientInfusionPatientDto == null && outpatientInfusionPatientDto.getPatientId() != null) {
            return null;
        }
        // 创建查询包装器
        LambdaQueryWrapper<OutpatientInfusionRecordDto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OutpatientInfusionRecordDto::getPatientId, outpatientInfusionPatientDto.getPatientId());
        // based_on_id 是为空的
        queryWrapper.eq(OutpatientInfusionRecordDto::getBasedOnId, null);
        // 状态是未完成的
        queryWrapper.in(OutpatientInfusionRecordDto::getServiceStatus, EventStatus.IN_PROGRESS.getValue(),
            EventStatus.NOT_DONE.getValue());
        // 从数据库获取输液记录列表
        List<OutpatientInfusionRecordDto> infusionList =
            outpatientManageMapper.getOutpatientInfusionRecord(queryWrapper);

        return infusionList;
    }

    /**
     * 执行单个患者门诊输液
     *
     * @param exeCount 执行记录数
     * @param outpatientInfusionRecordDto 患者输液信息
     * @return 门诊输液记录列表
     */
    @Override
    public boolean editPatientInfusionRecord(OutpatientInfusionRecordDto outpatientInfusionRecordDto, Long exeCount) {

        // 根据执行人ID，通过登录userId获取
        Practitioner practitioner =
            practitionerService.getPractitionerByUserId(SecurityUtils.getLoginUser().getUserId());
        // 以执行人ID，获取执行人的身份类别
        PractitionerRole practitionerRole = practitionerRoleService.getPractitionerRoleById(practitioner.getId());
        if (practitioner == null || practitionerRole == null) {
            return false;
        }

        String busNo = AssignSeqUtil.formatString(outpatientInfusionRecordDto.getBusNo(), exeCount, 3);
        // 当输液未被全部执行时，可以修改继续执行，生成一条执行记录
        if ((BigDecimal.valueOf(exeCount)).compareTo(outpatientInfusionRecordDto.getMedicationAntity()) < 0) {
            // 当 exeCount 小于 medicationAntity 时，执行这里的代码
            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setPrescriptionNo(outpatientInfusionRecordDto.getPrescriptionNo())
                // 设置busNo，原来的服务请求编码为基础.执行次数
                .setBusNo(busNo)
                // 基于service_request的id
                .setBasedOnId(outpatientInfusionRecordDto.getServiceId())
                // 设置状态完成
                .setStatusEnum(EventStatus.COMPLETED.getValue())
                // 设置请求code 和原来一致
                .setActivityId(outpatientInfusionRecordDto.getActivityId())
                // 患者id
                .setPatientId(outpatientInfusionRecordDto.getPatientId())
                // 就诊id
                .setEncounterId(outpatientInfusionRecordDto.getEncounterId())
                // 执行人id，通过登录userId获取
                .setPerformerId(practitioner.getId())
                // 设置执行人身份类别
                .setPerformerTypeCode(practitionerRole.getRoleCode())
                // 设置执行日期为当前时间
                .setOccurrenceStartTime(DateUtils.getNowDate())
                // 默认执行时间加30min结束
                .setOccurrenceEndTime(DateUtils.addDateMinute(DateUtils.getNowDate(), 30));

            boolean result = serviceRequestService.save(serviceRequest);
            if (!result) {
                return false;
            }

            // 判断如果是执行该患者最后一次记录,更新原来的服请求状态
            if ((BigDecimal.valueOf(exeCount + 1)).compareTo(outpatientInfusionRecordDto.getMedicationAntity()) == 0) {
                // 以id为主条件更新服务申请管理表
                LambdaUpdateWrapper<ServiceRequest> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(ServiceRequest::getId, outpatientInfusionRecordDto.getServiceId())
                    .set(ServiceRequest::getStatusEnum, EventStatus.COMPLETED.getValue())
                    .set(ServiceRequest::getPerformerTypeCode, practitionerRole.getRoleCode())
                    .set(ServiceRequest::getPerformerId, practitioner.getId())
                    .set(ServiceRequest::getOccurrenceStartTime, DateUtils.getNowDate())
                    .set(ServiceRequest::getOccurrenceEndTime, DateUtils.getNowDate());
                int countUpdate = serviceRequestMapper.update(null, updateWrapper);
                if (countUpdate < 0) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * 执行患者门诊输液
     *
     * @param outpatientInfusionRecordDtoList 输液记录
     * @return 修改成功/失败
     */
    @Override
    public boolean batchEditPatientInfusionRecord(List<OutpatientInfusionRecordDto> outpatientInfusionRecordDtoList) {

        // 根据执行人ID，通过登录userId获取
        Practitioner practitioner =
            practitionerService.getPractitionerByUserId(SecurityUtils.getLoginUser().getUserId());
        // 根具执行人ID，获取执行人身份类型
        PractitionerRole practitionerRole = practitionerRoleService.getPractitionerRoleById(practitioner.getId());
        if (practitioner == null || practitionerRole == null) {
            return false;
        }

        // 按 groupId 分组
        Map<Long, List<OutpatientInfusionRecordDto>> groupedRecords = outpatientInfusionRecordDtoList.stream()
            .collect(Collectors.groupingBy(OutpatientInfusionRecordDto::getGroupId));

        // 遍历每个分组
        for (Map.Entry<Long, List<OutpatientInfusionRecordDto>> entry : groupedRecords.entrySet()) {
            // Long groupId = entry.getKey();
            List<OutpatientInfusionRecordDto> groupRecords = entry.getValue();

            // 获取执行次数
            Long exeCount = serviceRequestService.countServiceRequestByBasedOnId(groupRecords.get(0).getBasedOnId());

            // 批量处理每个分组中的记录
            for (OutpatientInfusionRecordDto record : groupRecords) {
                boolean result = editSingleRecord(record, exeCount, practitioner, practitionerRole);
                if (!result) {
                    return false; // 如果某个记录执行失败，返回 false
                }
            }

            // 更新分组中每个记录的状态
            for (OutpatientInfusionRecordDto record : groupRecords) {

                if (!updateRecordStatus(record.getServiceId(), practitioner, practitionerRole)) {
                    return false; // 如果更新状态失败，返回 false
                }
            }
        }

        return true; // 所有分组都执行成功
    }

    /**
     * 处理单个记录的执行逻辑
     *
     * @param record 输液记录
     * @param practitioner 执行人
     * @param practitionerRole 执行人身份类型
     * @return 执行成功/失败
     */
    public boolean editSingleRecord(OutpatientInfusionRecordDto record, Long exeCount, Practitioner practitioner,
        PractitionerRole practitionerRole) {

        String busNo = AssignSeqUtil.formatString(record.getBusNo(), exeCount, 3);

        if ((BigDecimal.valueOf(exeCount)).compareTo(record.getMedicationAntity()) < 0) {
            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setPrescriptionNo(record.getPrescriptionNo()).setBusNo(busNo)
                .setBasedOnId(record.getServiceId()).setStatusEnum(EventStatus.COMPLETED.getValue())
                .setActivityId(record.getActivityId()).setPatientId(record.getPatientId())
                .setEncounterId(record.getEncounterId()).setPerformerId(practitioner.getId())
                .setPerformerTypeCode(practitionerRole.getRoleCode()).setOccurrenceStartTime(DateUtils.getNowDate())
                .setOccurrenceEndTime(DateUtils.addDateMinute(DateUtils.getNowDate(), 30));

            boolean result = serviceRequestService.save(serviceRequest);
            if (!result) {
                return false;
            }
        }

        return true;
    }

    /**
     * 更新执行状态
     *
     * @param serviceId 服务请求ID
     * @param practitioner 执行人
     * @param practitionerRole 执行人身份类型
     * @return 修改成功/失败
     */
    public boolean updateRecordStatus(Long serviceId, Practitioner practitioner, PractitionerRole practitionerRole) {
        LambdaUpdateWrapper<ServiceRequest> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ServiceRequest::getId, serviceId)
            .set(ServiceRequest::getStatusEnum, EventStatus.COMPLETED.getValue())
            .set(ServiceRequest::getPerformerTypeCode, practitionerRole.getRoleCode())
            .set(ServiceRequest::getPerformerId, practitioner.getId())
            .set(ServiceRequest::getOccurrenceStartTime, DateUtils.getNowDate())
            .set(ServiceRequest::getOccurrenceEndTime, DateUtils.getNowDate());

        int countUpdate = serviceRequestMapper.update(null, updateWrapper);
        return countUpdate > 0;
    }

    /**
     * 执行输液后,修改执行结束时间
     *
     * @param outpatientInfusionRecordDto 患者输液信息
     * @return 修改成功/失败
     */
    @Override
    public boolean editPatientInfusionTime(OutpatientInfusionRecordDto outpatientInfusionRecordDto) {
        // 以id为主条件更新服务申请管理表
        LambdaUpdateWrapper<ServiceRequest> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ServiceRequest::getId, outpatientInfusionRecordDto.getServiceId()).set(
            ServiceRequest::getOccurrenceEndTime,
            DateUtils.parseDate(outpatientInfusionRecordDto.getOccurrenceEndTime()));
        int countUpdate = serviceRequestMapper.update(null, updateWrapper);

        return countUpdate < 0 ? false : true;

    }

    /**
     * 显示门诊输液执行记录查询
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 门诊输液执行记录查询
     */
    @Override
    public List<OutpatientInfusionRecordDto> getPatientInfusionPerformRecord(String beginTime, String endTime) {

        LocalDateTime beginDateTime;
        LocalDateTime endDateTime;
        // 筛选时间不传,默认当天记录
        if (beginTime == null || endTime == null) {
            beginDateTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), true);
            endDateTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), false);
        } else {
            beginDateTime = DateUtils.startDayOrEndDay(beginTime, true);
            endDateTime = DateUtils.startDayOrEndDay(endTime, false);
        }

        // 创建查询包装器
        LambdaQueryWrapper<OutpatientInfusionRecordDto> queryWrapper = new LambdaQueryWrapper<>();
        // based_on_id 不为空
        queryWrapper.isNotNull(OutpatientInfusionRecordDto::getBasedOnId);
        // 状态是已完成
        queryWrapper.eq(OutpatientInfusionRecordDto::getServiceStatus, EventStatus.COMPLETED.getValue());
        // 时间筛选
        queryWrapper.ge(OutpatientInfusionRecordDto::getCreateTime, beginDateTime);
        queryWrapper.le(OutpatientInfusionRecordDto::getCreateTime, endDateTime);

        // 从数据库获取输液记录列表
        List<OutpatientInfusionRecordDto> infusionPerformList =
            outpatientManageMapper.getOutpatientInfusionRecord(queryWrapper);

        return infusionPerformList;
    }

}
