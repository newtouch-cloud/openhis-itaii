package com.openhis.web.outpatientmanage.appservice.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestInitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.mapper.PractitionerMapper;
import com.openhis.administration.mapper.PractitionerRoleMapper;
import com.openhis.administration.service.IPractitionerRoleService;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.clinical.domain.AllergyIntolerance;
import com.openhis.clinical.mapper.AllergyIntoleranceMapper;
import com.openhis.clinical.service.IAllergyIntoleranceService;
import com.openhis.common.enums.ClinicalStatus;
import com.openhis.common.enums.EventStatus;
import com.openhis.common.enums.VerificationStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.outpatientmanage.appservice.IOutpatientSkinTestRecordService;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordSearchParam;
import com.openhis.web.outpatientmanage.mapper.OutpatientManageMapper;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.mapper.ServiceRequestMapper;
import com.openhis.workflow.service.IServiceRequestService;

/**
 * 门诊管理 应用实现类
 *
 * @author liuhr
 * @date 2025/3/7
 */
@Service
public class OutpatientSkinTestRecordServiceImpl implements IOutpatientSkinTestRecordService {

    @Resource
    OutpatientManageMapper outpatientManageMapper;

    @Autowired
    ServiceRequestMapper serviceRequestMapper;

    @Autowired
    PractitionerMapper practitionerMapper;

    @Autowired
    PractitionerRoleMapper practitionerRoleMapper;

    @Autowired
    IAllergyIntoleranceService allergyIntoleranceService;

    @Autowired
    IPractitionerRoleService practitionerRoleService;

    @Autowired
    IPractitionerService practitionerService;

    @Autowired
    AllergyIntoleranceMapper allergyIntoleranceMapper;

    @Autowired
    IServiceRequestService serviceRequestService;


    /**
     * 获取门诊皮试记录初期数据列表
     *
     * @return 获取门诊皮试记录初期数据列表
     */
    @Override public OutpatientSkinTestInitDto getOutpatientSkinTestInit() {
        OutpatientSkinTestInitDto initDto = new OutpatientSkinTestInitDto();
        //获取皮试状态
        List<OutpatientSkinTestInitDto.statusEnumOption> statusEnumOptions1 = Stream.of(VerificationStatus.values())
            .map(status -> new OutpatientSkinTestInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setVerificationStatus(statusEnumOptions1);

        // 获取皮试结果
        List<OutpatientSkinTestInitDto.statusEnumOption> statusEnumOptions2 = Stream.of(ClinicalStatus.values())
            .map(status -> new OutpatientSkinTestInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setClinicalStatus(statusEnumOptions2);

        return initDto;
    }

    /**
     * 分页查询门诊皮试记录,可选条件
     *
     * @param outpatientSkinTestRecordSearchParam 查询条件
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    @Override
    public Page<OutpatientSkinTestRecordDto> getSkinTestRecords(
        OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam, Integer pageNo, Integer pageSize) {
        // 跳过的记录数
        Integer offset = (pageNo - 1) * pageSize;
        // 连表查询患者信息
        List<OutpatientSkinTestRecordDto> listOutpatientSkinTestRecords =
            outpatientManageMapper.getSkinTestRecords(outpatientSkinTestRecordSearchParam, pageSize, offset);

        // 查询总记录数
        long total = outpatientManageMapper.countOutpatientSkinTestRecords(outpatientSkinTestRecordSearchParam);
        // 创建Page对象并设置属性
        Page<OutpatientSkinTestRecordDto> outpatientSkinTestRecordPage = new Page<>(pageNo, pageSize, total);
        outpatientSkinTestRecordPage.setRecords(listOutpatientSkinTestRecords);
        outpatientSkinTestRecordPage.getRecords().forEach(e -> {
            // 皮试结果状态枚举类回显赋值
            e.setClinicalStatusEnum_enumText(EnumUtils.getInfoByValue(ClinicalStatus.class, e.getClinicalStatusEnum()));
            // 皮试检查项目状态枚举类回显赋值
            e.setVerificationStatusEnum_enumText(
                EnumUtils.getInfoByValue(VerificationStatus.class, e.getVerificationStatusEnum()));
            // 药品状态状态枚举类回显赋值
            e.setMedicationStatusEnum_enumText(
                EnumUtils.getInfoByValue(EventStatus.class, e.getMedicationStatusEnum()));
        });
        return outpatientSkinTestRecordPage;
    }

    /**
     * 护士确认执行皮试后，更新皮试记录信息（服务申请管理与过敏与不耐受的相关字段更新）
     *
     * @param outpatientSkinTestRecordDto 皮试记录信息
     */
    @Override
    public boolean editSkinTestRecord(OutpatientSkinTestRecordDto outpatientSkinTestRecordDto) {
        // 判断核对人是否不为空,药品状态不是已发药
        if (outpatientSkinTestRecordDto.getPerformerCheckId() != null
            || outpatientSkinTestRecordDto.getMedicationStatusEnum() != EventStatus.COMPLETED.getValue()) {
            // 签名后不能修改，未发药不能修改
            return false;
        }

        // 更新服务申请管理表
        ServiceRequest serviceRequest = new ServiceRequest();
        // 更新的条件
        serviceRequest.setId(outpatientSkinTestRecordDto.getId());

        // 判断开始时间为空，不允许更新表
        if (StringUtils.isEmpty(outpatientSkinTestRecordDto.getOccurrenceStartTime())) {
            return false;
        }
        Date endTime;
        // 判断结束时间，为空以开始时间基础加10分钟
        if (StringUtils.isEmpty(outpatientSkinTestRecordDto.getOccurrenceEndTime())) {
            // 结束时间为空，开始时间加10min设置
            endTime =
                DateUtils.addDateMinute(DateUtils.parseDate(outpatientSkinTestRecordDto.getOccurrenceStartTime()), 10);
        } else {
            endTime = DateUtils.parseDate(outpatientSkinTestRecordDto.getOccurrenceEndTime());
        }
        // 设置开始时间
        serviceRequest
            .setOccurrenceStartTime(DateUtils.parseDate(outpatientSkinTestRecordDto.getOccurrenceStartTime()));
        // 设置结束时间
        serviceRequest.setOccurrenceEndTime(endTime);

        // 获取系统登录的userId，找到practitionerId
        Practitioner practitioner =
            practitionerService.getPractitionerByUserId(SecurityUtils.getLoginUser().getUserId());
        if (practitioner == null) {
            return false;
        }
        // 设置执行人ID
        serviceRequest.setPerformerId(practitioner.getId());

        // 以执行人ID，获取执行人的身份类别
        PractitionerRole practitionerRole = practitionerRoleService.getPractitionerRoleById(practitioner.getId());
        if (practitionerRole != null) {
            // 设置执行人身份类别
            serviceRequest.setPerformerTypeCode(practitionerRole.getRoleCode());
        }

        // 以id为主条件更新服务申请管理表
        UpdateWrapper<ServiceRequest> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", serviceRequest.getId()).set("performer_type_code", serviceRequest.getPerformerTypeCode())
            .set("performer_id", serviceRequest.getPerformerId())
            .set("occurrence_start_time", serviceRequest.getOccurrenceStartTime())
            .set("occurrence_end_time", serviceRequest.getOccurrenceEndTime());

        int countUpdate = serviceRequestMapper.update(null, updateWrapper);

        // 过敏与不耐受表更新
        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
        if (outpatientSkinTestRecordDto.getClinicalStatusEnum() != null) {
            // 设置临床状态（皮试结果）
            allergyIntolerance.setClinicalStatusEnum(outpatientSkinTestRecordDto.getClinicalStatusEnum());
        }
        allergyIntolerance
            // 设置服务申请ID
            .setRequestId(outpatientSkinTestRecordDto.getId())
            // 设置验证状态（皮试检查的状态）
            .setVerificationStatusEnum(outpatientSkinTestRecordDto.getVerificationStatusEnum())
            // 设置患者id
            .setPatientId(outpatientSkinTestRecordDto.getPatientId())
            // 设置记录者id
            .setPractitionerId(practitioner.getId())
            // 设置记录日期(当下日期)
            .setRecordedDate(DateUtils.getNowDate())
            // 设置备注
            .setNote(outpatientSkinTestRecordDto.getNote());

        // 以服务申请ID为主条件更新过敏与不耐受表
        UpdateWrapper<AllergyIntolerance> updateWrapperAI = new UpdateWrapper<>();
        updateWrapperAI.eq("request_id", allergyIntolerance.getRequestId());
        boolean result = allergyIntoleranceService.saveOrUpdate(allergyIntolerance, updateWrapperAI);
        // 更新或插入失败
        if (!result || countUpdate <= 0) {
            return false;
        }

        return true;
    }

    @Override
    public boolean nurseSignChkPs(OutpatientSkinTestRecordDto outpatientSkinTestRecordDto) {

        // 过敏与不耐受表更新
        QueryWrapper<AllergyIntolerance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("request_id", outpatientSkinTestRecordDto.getId());
        AllergyIntolerance allergyIntolerance = allergyIntoleranceMapper.selectOne(queryWrapper);

        // 检查的状态是确定和反驳的时候，不更新
        if (allergyIntolerance == null
            || (allergyIntolerance.getVerificationStatusEnum() != VerificationStatus.CONFIRMED.getValue()
                && allergyIntolerance.getVerificationStatusEnum() != VerificationStatus.REFUTED.getValue())) {
            return false;
        }

        // 更新服务申请管理表的
        ServiceRequest serviceRequest = new ServiceRequest();
        // 获取系统登录的userId，找到practitionerId
        Practitioner practitioner =
            practitionerService.getPractitionerByUserId(SecurityUtils.getLoginUser().getUserId());
        // 找不到找到practitionerId时，不更新
        if (practitioner == null) {
            return false;
        }
        // 设置核对人ID
        serviceRequest.setPerformerCheckId(practitioner.getId());
        // 把服务请求的状态设置为已完成
        serviceRequest.setStatusEnum(EventStatus.COMPLETED.getValue());
        // 以id为主条件更新服务申请管理表
        UpdateWrapper<ServiceRequest> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", outpatientSkinTestRecordDto.getId())
            .set("performer_check_id", serviceRequest.getPerformerCheckId())
            .set("status_enum", serviceRequest.getStatusEnum());

        boolean resultUpdateRequestService = serviceRequestService.update(null, updateWrapper);

        // 设置断言人
        allergyIntolerance.setCheckPractitionerId(practitioner.getId());

        // 当皮试结果是为阳性的时候，设置过敏时间
        if (allergyIntolerance.getClinicalStatusEnum() == ClinicalStatus.ACTIVE.getValue()) {
            // 设置过敏时间(皮实结束时间)
            allergyIntolerance
                .setOnsetDateTime(DateUtils.parseDate(outpatientSkinTestRecordDto.getOccurrenceEndTime()));
        }

        // 以服务申请ID为主条件更新服务申请管理表
        UpdateWrapper<AllergyIntolerance> updateWrapperAI = new UpdateWrapper<>();
        updateWrapperAI.eq("request_id", outpatientSkinTestRecordDto.getId())
            .set("check_practitioner_id", allergyIntolerance.getCheckPractitionerId())
            .set("onset_date_time", allergyIntolerance.getOnsetDateTime());
        boolean resultUpdateAllergyIntolerance = allergyIntoleranceService.update(null, updateWrapperAI);

        if (resultUpdateRequestService && resultUpdateAllergyIntolerance) {
            return true;
        }

        return false;
    }

}
