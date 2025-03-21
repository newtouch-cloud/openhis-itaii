package com.openhis.web.outpatientmanage.appservice.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.openhis.common.enums.EventStatus;
import com.openhis.common.enums.Whether;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.outpatientmanage.appservice.IOutpatientInfusionRecordService;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
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
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 分页查询
     */
    @Override
    public IPage<OutpatientInfusionPatientDto> getOutpatientInfusionPatientList(String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<OutpatientInfusionPatientDto> queryWrapper =
            HisQueryUtils.buildQueryWrapper(new OutpatientInfusionPatientDto(), searchKey,
                new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientBusNo,
                    CommonConstants.FieldName.EncounterBusNo, CommonConstants.FieldName.PatientName)),
                request);

        IPage<OutpatientInfusionPatientDto> outpatientInfusionPatientDto =
            outpatientManageMapper.getOutpatientInfusionPatient(new Page<>(pageNo, pageSize), queryWrapper);

        outpatientInfusionPatientDto.getRecords().forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAgeString(AgeCalculatorUtil.getAge(DateUtils.parseDate(e.getBirthDate())));
        });

        return outpatientInfusionPatientDto;
    }

    /**
     * 点击患者，查询该患者的输液记录
     *
     * @param outpatientInfusionPatientDto 患者输液信息
     * @return 当前患者门诊输液待执行列表
     */
    @Override
    public List<OutpatientInfusionRecordDto> getPatientInfusionRecord(
        OutpatientInfusionPatientDto outpatientInfusionPatientDto, HttpServletRequest request) {

        // 参数不能为空
        if (outpatientInfusionPatientDto == null) {
            return null;
        } else if (outpatientInfusionPatientDto.getPatientId() == null) {
            return null;
        }

        // 构建查询条件
        QueryWrapper<OutpatientInfusionRecordDto> queryWrapper =
            HisQueryUtils.buildQueryWrapper(new OutpatientInfusionPatientDto(), null, null, request);

        queryWrapper.eq(CommonConstants.FieldName.PatientId, outpatientInfusionPatientDto.getPatientId());
        // based_on_id 是为空的
        queryWrapper.isNull(CommonConstants.FieldName.BasedOnId);
        // 状态是未完成的
        queryWrapper.eq(CommonConstants.FieldName.RequestStatus, EventStatus.IN_PROGRESS.getValue());

        // 默认显示100条
        IPage<OutpatientInfusionRecordDto> OutpatientInfusionRecordPage =
            outpatientManageMapper.getOutpatientInfusionRecord(new Page<>(1, 100), queryWrapper);

        List<OutpatientInfusionRecordDto> infusionPerformList = OutpatientInfusionRecordPage.getRecords();

        // 遍历列表并处理每个记录
        infusionPerformList.forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 药品状态
            e.setMedicationStatusEnum_enumText(
                EnumUtils.getInfoByValue(EventStatus.class, e.getMedicationStatusEnum()));
            // 皮试标志
            e.setSkinTestFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getSkinTestFlag()));

        });

        return infusionPerformList;
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

        // 按 serviceId 分组
        Map<Long, List<OutpatientInfusionRecordDto>> serviceRecords = outpatientInfusionRecordDtoList.stream()
            .collect(Collectors.groupingBy(OutpatientInfusionRecordDto::getServiceId));

        // 构造批量插入的 ServiceRequest 列表
        List<ServiceRequest> serviceRequests = new ArrayList<>();

        // 遍历每个分组
        for (Map.Entry<Long, List<OutpatientInfusionRecordDto>> entry : groupedRecords.entrySet()) {
            List<OutpatientInfusionRecordDto> groupRecords = entry.getValue();

            // 获取组内药品个数
            Long groupCount = outpatientManageMapper.countExecuteNumOrGroupNum(groupRecords.get(0).getServiceId(), null,
                groupRecords.get(0).getGroupId(), false);

            // 检查组内药品是否全部选中
            if (groupCount != groupRecords.size()) {
                return false;
            }

            for (OutpatientInfusionRecordDto record : groupRecords) {

                // 判断当前组药品状态不是已领药，有未领药的数据，跳过执行下一组数据
                if (!(record.getMedicationStatusEnum() == EventStatus.COMPLETED.getValue())) {
                    // 跳过当前分组的剩余处理，继续下一个分组
                    break;
                }

                String prefixBusNo = record.getBusNo() + CommonConstants.Common.DASH + record.getGroupId()
                    + CommonConstants.Common.DASH + record.getMedicationId();
                // 获取执行次数
                Long exeCount =
                    outpatientManageMapper.countExecuteNumOrGroupNum(record.getServiceId(), prefixBusNo, null, true);

                if (exeCount < record.getExecuteNum()) {
                    ServiceRequest serviceRequest = new ServiceRequest();
                    serviceRequest.setPrescriptionNo(record.getPrescriptionNo())
                        .setBusNo(AssignSeqUtil.formatString(prefixBusNo, exeCount, 3))
                        .setBasedOnId(record.getServiceId()).setStatusEnum(EventStatus.COMPLETED.getValue())
                        .setActivityId(record.getActivityId()).setPatientId(record.getPatientId())
                        .setEncounterId(record.getEncounterId()).setPerformerId(practitioner.getId())
                        .setPerformerTypeCode(practitionerRole.getRoleCode())
                        .setOccurrenceStartTime(DateUtils.getNowDate()).setRequesterId(practitioner.getId())
                        .setOccurrenceEndTime(DateUtils.addDateMinute(DateUtils.getNowDate(), 30));

                    serviceRequests.add(serviceRequest);
                }
            }
        }

        // 使用 MyBatis-Plus 的 saveBatch 方法批量插入
        if (!serviceRequestService.saveBatch(serviceRequests)) {
            return false; // 如果批量插入失败，返回 false
        }
        // 批量更新执行状态
        List<Long> serviceIds = checkServiceRequestIsCompleted(serviceRecords);
        int updateRes = batchUpdateRecordStatus(serviceIds);

        // 如果更新失败，返回 false
        if (updateRes == 0) {
            return false;
        }

        return true;

    }

    /**
     * 检查该条服务申请的所有输液信息都完成
     *
     * @param serviceRecords 同一个服务请求的输液信息列表
     * @return 更新的ID集合
     */
    public List<Long> checkServiceRequestIsCompleted(Map<Long, List<OutpatientInfusionRecordDto>> serviceRecords) {

        // 存储更新的serviceId
        List<Long> serviceIds = new ArrayList<>();

        // 遍历每个分组
        for (Map.Entry<Long, List<OutpatientInfusionRecordDto>> entry : serviceRecords.entrySet()) {
            // 获取当前分组的 serviceId
            Long serviceId = entry.getKey();
            // 获取当前分组的记录列表
            List<OutpatientInfusionRecordDto> groupRecords = entry.getValue();

            // 创建一个数组来记录每个记录是否满足条件
            int[] flags = new int[serviceRecords.size()];
            int index = 0; // 用于记录当前索引

            for (OutpatientInfusionRecordDto record : groupRecords) {
                String prefixBusNo = record.getBusNo() + CommonConstants.Common.DASH + record.getGroupId()
                    + CommonConstants.Common.DASH + record.getMedicationId();
                // 获取执行次数
                Long exeCount = outpatientManageMapper.countExecuteNumOrGroupNum(serviceId, prefixBusNo, null, true);

                // 判断是否满足条件
                if (exeCount.equals(record.getExecuteNum())) {
                    flags[index] = 1; // 如果满足条件，设置为 1
                } else {
                    flags[index] = 0; // 如果不满足条件，设置为 0
                }
                index++;
            }

            // 使用与运算判断最终结果
            int result = 1;
            for (int flag : flags) {
                result &= flag; // 与运算
            }

            // 如果最终结果为 1，表示所有记录都满足条件,即当前服务请求的数据全部被执行
            if (result == 1) {
                serviceIds.add(serviceId);
                return serviceIds;
            }

        }

        return null;
    }

    /**
     * 批量更新执行状态
     *
     * @param serviceIds 服务请求ID列表
     * @return 修改成功/失败
     */
    public int batchUpdateRecordStatus(List<Long> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            // 返回-1,表示不更新
            return -1;
        }

        LambdaUpdateWrapper<ServiceRequest> updateWrapper = new LambdaUpdateWrapper<>();
        // 使用 IN 条件匹配多个 serviceId
        updateWrapper.in(ServiceRequest::getId, serviceIds).set(ServiceRequest::getStatusEnum,
            EventStatus.COMPLETED.getValue());

        int countUpdate = serviceRequestMapper.update(null, updateWrapper);
        // 如果更新的记录数大于 0
        return countUpdate;
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

        return countUpdate >= 0;

    }

    /**
     * 显示门诊输液执行记录查询
     *
     * @param historyFlag 查询的是否为执行履历
     * @return 返回门诊输液执行记录查询
     */
    @Override
    public List<OutpatientInfusionRecordDto> getPatientInfusionPerformRecord(HttpServletRequest request,
        boolean historyFlag) {

        // 构建查询条件
        QueryWrapper<OutpatientInfusionRecordDto> queryWrapper =
            HisQueryUtils.buildQueryWrapper(new OutpatientInfusionRecordDto(), null, null, request);

        // 执行历史查询的条件
        if (historyFlag) {
            // based_on_id 不为空，此条件筛选出执行履历
            queryWrapper.isNotNull(CommonConstants.FieldName.BasedOnId);
            // 状态是已完成
            queryWrapper.eq(CommonConstants.FieldName.RequestStatus, EventStatus.COMPLETED.getValue());

            List<OutpatientInfusionRecordDto> infusionPerformList = editRecords(queryWrapper);
            List<Long> medicationIds = checkServiceRequestIsCompleted(infusionPerformList);
            // 未产生执行历史
            if (medicationIds == null || medicationIds.isEmpty()) {
                return infusionPerformList;
            }
            // 筛选一下执行的药品
            queryWrapper.in(CommonConstants.FieldName.MedicationId, medicationIds);

            return editRecords(queryWrapper);

            // 门诊输液待执行记录查询
        } else {
            // based_on_id 为空，此条件筛选控制不显示执行履历
            queryWrapper.isNull(CommonConstants.FieldName.BasedOnId);
            // 状态是进行中
            queryWrapper.eq(CommonConstants.FieldName.RequestStatus, EventStatus.IN_PROGRESS.getValue());

            return editRecords(queryWrapper);
        }

    }

    /**
     * 执行完成后，显示执行历史
     *
     * @param infusionPerformList 执行历史输液列表
     * @return medicationIds 返回药品id集合
     */
    public List<Long> checkServiceRequestIsCompleted(List<OutpatientInfusionRecordDto> infusionPerformList) {

        List<Long> medicationIds = new ArrayList<>();
        infusionPerformList.forEach(e -> {

            // 使用正则表达式匹配 . 前面的第一个数字
            String regex = "(\\d+)\\.";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(e.getBusNo());

            if (matcher.find()) {
                // 获取匹配的第一个数字
                medicationIds.add(Long.valueOf(matcher.group(1))); // 添加到 List 中
            }
        });

        return medicationIds;

    }

    /**
     * 显示门诊输液执行记录查询
     *
     * @param queryWrapper 查询条件
     * @return 返回门诊输液执行记录查询
     */
    public List<OutpatientInfusionRecordDto> editRecords(QueryWrapper<OutpatientInfusionRecordDto> queryWrapper) {

        // 默认显示100条
        IPage<OutpatientInfusionRecordDto> OutpatientInfusionRecordPage =
            outpatientManageMapper.getOutpatientInfusionRecord(new Page<>(1, 100), queryWrapper);
        List<OutpatientInfusionRecordDto> infusionPerformList = OutpatientInfusionRecordPage.getRecords();

        // 遍历列表并处理每个记录
        infusionPerformList.forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 药品状态
            e.setMedicationStatusEnum_enumText(
                EnumUtils.getInfoByValue(EventStatus.class, e.getMedicationStatusEnum()));
            // 皮试标志
            e.setSkinTestFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getSkinTestFlag()));

        });

        return infusionPerformList;
    }

}
