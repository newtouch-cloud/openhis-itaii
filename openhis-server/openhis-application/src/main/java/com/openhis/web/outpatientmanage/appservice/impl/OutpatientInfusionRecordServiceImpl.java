package com.openhis.web.outpatientmanage.appservice.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.enums.ClinicalStatus;
import com.openhis.common.enums.EventStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.outpatientmanage.appservice.IOutpatientInfusionRecordService;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionInitDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionSearchParam;
import com.openhis.web.outpatientmanage.mapper.OutpatientManageMapper;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.service.IServiceRequestService;

/**
 * 门诊管理——输液实现类
 *
 * @author liuhr
 * @date 2025/3/12
 */
@Service
public class OutpatientInfusionRecordServiceImpl implements IOutpatientInfusionRecordService {

    @Resource
    OutpatientManageMapper outpatientManageMapper;

    @Autowired
    IServiceRequestService serviceRequestService;

    @Autowired
    IPractitionerService practitionerService;

    @Override
    /**
     * 获取门诊输液记录初期数据列表
     *
     * @return 门诊输液记录初期数据列表
     */
    public OutpatientInfusionInitDto getOutpatientInfusionInit() {
        OutpatientInfusionInitDto initDto = new OutpatientInfusionInitDto();
        // 获取药品状态
        List<OutpatientInfusionInitDto.statusEnumOption> statusEnumOptions = Stream.of(EventStatus.values())
            .map(status -> new OutpatientInfusionInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setMedicationStatus(statusEnumOptions);

        // 获取皮试结果
        List<OutpatientInfusionInitDto.statusEnumOption> statusEnumOptions2 = Stream.of(ClinicalStatus.values())
            .map(status -> new OutpatientInfusionInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setClinicalStatus(statusEnumOptions2);

        // 获取当天日期
        LocalDateTime beginTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), true);
        LocalDateTime endTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), false);
        // 创建查询包装器
        QueryWrapper<OutpatientInfusionRecordDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("occurrence_start_time", beginTime);
        queryWrapper.le("occurrence_end_time", endTime);

        // 从数据库获取输液记录列表
        List<OutpatientInfusionRecordDto> infusionList =
            outpatientManageMapper.getOutpatientInfusionRecord(queryWrapper);
        // 遍历列表并处理每个记录
        infusionList.forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 药品状态
            e.setClinicalStatusEnum_enumText(EnumUtils.getInfoByValue(EventStatus.class, e.getMedicationStatusEnum()));
            // 皮试结果
            e.setMedicationStatusEnum_enumText(
                EnumUtils.getInfoByValue(ClinicalStatus.class, e.getClinicalStatusEnum()));
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

        String searchKey;
        LocalDateTime beginTime;
        LocalDateTime endTime;
        if (outpatientInfusionSearchParam == null) {
            searchKey = null;
            beginTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), true);
            endTime = DateUtils.startDayOrEndDay(DateUtils.getDate(), false);
        } else {
            searchKey = outpatientInfusionSearchParam.getSearchKey();
            beginTime = DateUtils.startDayOrEndDay(outpatientInfusionSearchParam.getBeginTime(), true);
            endTime = DateUtils.startDayOrEndDay(outpatientInfusionSearchParam.getEndTime(), false);
        }

        // 构建查询条件
        QueryWrapper<OutpatientInfusionPatientDto> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
            new HashSet<>(Arrays.asList("patient_busNo", "encounter_busNo", "patient_name")), null);
        // 添加时间段查询条件
        if (beginTime != null && endTime != null) {
            queryWrapper.ge("begin_time", beginTime);
            queryWrapper.le("end_time", endTime);
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
        QueryWrapper<OutpatientInfusionRecordDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_id", outpatientInfusionPatientDto.getPatientId());

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
        // 这里执行一次就insert 表 wor_service_request里一条数据，based_on_id一直，以mr.quantity来振分
        // 点击执行一次，生成一条执行记录，确认执行+1 ，直至所有药品打完，

        // 根据执行人ID，通过登录userId获取
        Practitioner practitioner =
            practitionerService.getPractitionerByUserId(SecurityUtils.getLoginUser().getUserId());
        if (practitioner == null) {
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
                //患者id
                .setPatientId(outpatientInfusionRecordDto.getPatientId())
                //就诊id
                .setEncounterId(outpatientInfusionRecordDto.getEncounterId())
                // 执行人id，通过登录userId获取
                .setPerformerId(practitioner.getId())
                // 设置执行日期为当前时间
                .setOccurrenceStartTime(DateUtils.getNowDate())
                // 默认30结束
                .setOccurrenceEndTime(DateUtils.addDateMinute(DateUtils.getNowDate(), 30));
            boolean result = serviceRequestService.save(serviceRequest);
            if (!result) {
                return false;
            }

            return true;
        }

        return false;

    }

}
