package com.openhis.web.outpatientmanage.appservice.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
     * 获取门诊输液记录的患者列表
     *
     * @param outpatientInfusionSearchParam 门诊输液记录的患者列表查询参数
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 分页查询
     */
    @Override
    public IPage<OutpatientInfusionPatientDto> getOutpatientInfusionPatientList(
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
        queryWrapper.eq(CommonConstants.FieldName.basedOnId, null);
        // 状态是未完成的
        queryWrapper.in(CommonConstants.FieldName.requestStatus, EventStatus.IN_PROGRESS.getValue(),
            EventStatus.NOT_DONE.getValue());
        // 添加时间段查询条件
        if (beginTime != null && endTime != null) {
            queryWrapper.ge(CommonConstants.FieldName.createTime, beginTime);
            queryWrapper.le(CommonConstants.FieldName.createTime, endTime);
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
     * 点击患者，执行该患者的输液记录
     *
     * @param outpatientInfusionPatientDto 患者输液信息
     * @return 当前患者门诊输液待执行列表
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
        queryWrapper.in(OutpatientInfusionRecordDto::getRequestStatus, EventStatus.IN_PROGRESS.getValue(),
            EventStatus.NOT_DONE.getValue());
        // 从数据库获取输液记录列表
        List<OutpatientInfusionRecordDto> infusionList =
            outpatientManageMapper.getOutpatientInfusionRecord(queryWrapper);

        return infusionList;
    }

    /**
     * 执行患者门诊输液
     *
     * @param outpatientInfusionRecordDtoList 输液记录列表
     * @return 执行成功/失败
     */
    @Override
    public boolean batchEditPatientInfusionRecord(List<OutpatientInfusionRecordDto> outpatientInfusionRecordDtoList) {
        // 根据执行人ID，通过登录userId获取
        Practitioner practitioner =
            practitionerService.getPractitionerByUserId(SecurityUtils.getLoginUser().getUserId());
        // 根据执行人ID，获取执行人身份类型
        PractitionerRole practitionerRole = practitionerRoleService.getPractitionerRoleById(practitioner.getId());
        if (practitioner == null || practitionerRole == null) {
            return false;
        }

        // 按 groupId 分组
        Map<Long, List<OutpatientInfusionRecordDto>> groupedRecords = outpatientInfusionRecordDtoList.stream()
            .collect(Collectors.groupingBy(OutpatientInfusionRecordDto::getGroupId));

        // 遍历每个分组
        for (Map.Entry<Long, List<OutpatientInfusionRecordDto>> entry : groupedRecords.entrySet()) {
            List<OutpatientInfusionRecordDto> groupRecords = entry.getValue();

            // 获取组内药品个数
            Long groupCount = outpatientManageMapper.countMedicationExecuteNum(groupRecords.get(0).getServiceId(), null,
                groupRecords.get(0).getGroupId(), false);
            // 检查组内药品是否全部选中
            if (groupCount != groupRecords.size()) {
                return false;
            }

            // 构造批量插入的 ServiceRequest 列表
            List<ServiceRequest> serviceRequests = new ArrayList<>();
            for (OutpatientInfusionRecordDto record : groupRecords) {
                String prefixBusNo = record.getBusNo() + "." + record.getGroupId() + "." + record.getMedicationId();
                // 获取执行次数
                Long exeCount =
                    outpatientManageMapper.countMedicationExecuteNum(record.getServiceId(), prefixBusNo, null, true);

                if (exeCount < record.getExecuteNum()) {
                    ServiceRequest serviceRequest = new ServiceRequest();
                    serviceRequest.setPrescriptionNo(record.getPrescriptionNo())
                        .setBusNo(AssignSeqUtil.formatString(prefixBusNo, exeCount, 3))
                        .setBasedOnId(record.getServiceId()).setStatusEnum(EventStatus.COMPLETED.getValue())
                        .setActivityId(record.getActivityId()).setPatientId(record.getPatientId())
                        .setEncounterId(record.getEncounterId()).setPerformerId(practitioner.getId())
                        .setPerformerTypeCode(practitionerRole.getRoleCode())
                        .setOccurrenceStartTime(DateUtils.getNowDate())
                        .setOccurrenceEndTime(DateUtils.addDateMinute(DateUtils.getNowDate(), 30));

                    serviceRequests.add(serviceRequest);
                }
            }
            // 使用 MyBatis-Plus 的 saveBatch 方法批量插入
            if (!serviceRequestService.saveBatch(serviceRequests)) {
                return false; // 如果批量插入失败，返回 false
            }
            // 更新分组中每个记录的状态
            for (OutpatientInfusionRecordDto record : groupRecords) {
                String prefixBusNo = record.getBusNo() + "." + record.getGroupId() + "." + record.getMedicationId();
                // 获取执行次数
                Long exeCount =
                    outpatientManageMapper.countMedicationExecuteNum(record.getServiceId(), prefixBusNo, null, true);

                // 执行完毕后，更新执行服务请求表的状态
                if (exeCount.equals(record.getExecuteNum())) {
                    if (!updateRecordStatus(record.getServiceId())) {
                        return false; // 如果更新状态失败，返回 false
                    }
                }
            }
        }
        // 所有分组都执行成功
        return true;
    }

    /**
     * 更新执行状态
     *
     * @param serviceId 服务请求ID
     * @return 修改成功/失败
     */
    public boolean updateRecordStatus(Long serviceId) {
        LambdaUpdateWrapper<ServiceRequest> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ServiceRequest::getId, serviceId).set(ServiceRequest::getStatusEnum,
            EventStatus.COMPLETED.getValue());

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
     * @param historyFlag 查询的是否为执行履历
     * @return 门诊输液执行记录查询
     */
    @Override
    public List<OutpatientInfusionRecordDto> getPatientInfusionPerformRecord(String beginTime, String endTime,
        boolean historyFlag) {

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
        // 执行历史查询的条件
        if (historyFlag) {
            // based_on_id 不为空，此条件筛选出执行履历
            queryWrapper.isNotNull(OutpatientInfusionRecordDto::getBasedOnId);
            // 状态是已完成
            queryWrapper.eq(OutpatientInfusionRecordDto::getRequestStatus, EventStatus.COMPLETED.getValue());

            // 门诊输液待执行记录查询
        } else {
            // based_on_id 为空，此条件筛选控制不显示执行履历
            queryWrapper.isNull(OutpatientInfusionRecordDto::getBasedOnId);
            // 状态是进行中
            queryWrapper.eq(OutpatientInfusionRecordDto::getRequestStatus, EventStatus.IN_PROGRESS.getValue());
        }
        // 时间筛选
        queryWrapper.ge(OutpatientInfusionRecordDto::getCreateTime, beginDateTime);
        queryWrapper.le(OutpatientInfusionRecordDto::getCreateTime, endDateTime);

        // 从数据库获取输液记录列表
        List<OutpatientInfusionRecordDto> infusionPerformList =
            outpatientManageMapper.getOutpatientInfusionRecord(queryWrapper);

        // 遍历列表并处理每个记录
        infusionPerformList.forEach(e -> {
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

        return infusionPerformList;
    }

}
