package com.openhis.web.doctorstation.appservice.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.clinical.domain.AllergyIntolerance;
import com.openhis.clinical.mapper.AllergyIntoleranceMapper;
import com.openhis.clinical.service.IAllergyIntoleranceService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.ClinicalStatus;
import com.openhis.common.enums.Criticality;
import com.openhis.common.enums.Severity;
import com.openhis.common.enums.VerificationStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.doctorstation.appservice.IDoctorStationAllergyIntolAppService;
import com.openhis.web.doctorstation.dto.AllergyIntoInfoDto;
import com.openhis.web.doctorstation.dto.AllergyIntoInitDto;

/**
 * 医生站-患者过敏与不耐受管理的实现
 *
 * @author liuhr
 * @date 2025/4/10
 */
@Service
public class DoctorStationAllergyIntolAppServiceImpl implements IDoctorStationAllergyIntolAppService {

    @Autowired
    private IAllergyIntoleranceService allergyIntoleranceService;

    @Autowired
    private AllergyIntoleranceMapper allergyIntoleranceMapper;

    /**
     * 患者过敏与不耐受数据初始化
     *
     * @return 基础数据
     */
    @Override
    public R<?> init() {

        AllergyIntoInitDto initDto = new AllergyIntoInitDto();

        // 获取临床状况列表
        List<AllergyIntoInitDto.statusEnumOption> statusEnumOption1 = Stream.of(ClinicalStatus.values())
            .map(status -> new AllergyIntoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setClinicalStatusOptions(statusEnumOption1);

        // 获取临床状况列表
        List<AllergyIntoInitDto.statusEnumOption> statusEnumOption2 = Stream.of(VerificationStatus.values())
            .map(status -> new AllergyIntoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setVerificationStatusOptions(statusEnumOption2);

        // 获取危险程度列表
        List<AllergyIntoInitDto.statusEnumOption> statusEnumOption3 = Stream.of(Criticality.values())
            .map(status -> new AllergyIntoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setCriticalityOptions(statusEnumOption3);

        // 获取严重程度列表
        List<AllergyIntoInitDto.statusEnumOption> statusEnumOption4 = Stream.of(Severity.values())
            .map(status -> new AllergyIntoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setSeverityOptions(statusEnumOption4);

        return R.ok(initDto);

    }

    /**
     * 查询患者过敏与不耐受信息
     *
     * @param patientId 患者Id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 患者过敏与不耐受信息
     */
    @Override
    public R<?> getAllergyIntoleranceInfo(Long patientId, Integer pageNo, Integer pageSize,
        HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<AllergyIntolerance> queryWrapper = HisQueryUtils.buildQueryWrapper(null, null, null, request);

        // 根据患者ID查询过敏与不耐受记录
        queryWrapper.eq(CommonConstants.FieldName.PatientId, patientId);
        // 设置排序
        queryWrapper.orderByAsc(CommonConstants.FieldName.CreateTime);
        // 分页查询
        Page<AllergyIntoInfoDto> allergyIntolerancePage =
            HisPageUtils.selectPage(allergyIntoleranceMapper, queryWrapper, pageNo, pageSize, AllergyIntoInfoDto.class);

        allergyIntolerancePage.getRecords().forEach(e -> {
            // 临床状况回显赋值
            e.setClinicalStatusEnum_enumText(EnumUtils.getInfoByValue(ClinicalStatus.class, e.getClinicalStatusEnum()));
            // 验证状态回显赋值
            e.setVerificationStatusEnum_enumText(
                EnumUtils.getInfoByValue(VerificationStatus.class, e.getVerificationStatusEnum()));
            // 危险程度回显赋值
            e.setCriticalityEnum_enumText(EnumUtils.getInfoByValue(Criticality.class, e.getCriticalityEnum()));
            // 严重程度回显赋值
            e.setSeverityEnum_enumText(EnumUtils.getInfoByValue(Severity.class, e.getSeverityEnum()));

        });

        // 返回【患者过敏与不耐受信息】分页
        return R.ok(allergyIntolerancePage);

    }

    /**
     * 作废当条患者过敏与不耐受信息
     *
     * @param allergyIntoInfoDto 患者过敏与不耐受信息
     * @return
     */
    @Override
    public R<?> invalidateAllergyIntolerance(AllergyIntoInfoDto allergyIntoInfoDto) {

        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

        // 获取当前登录账号的参与者id
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();

        allergyIntolerance.setId(allergyIntoInfoDto.getId()).setClinicalStatusEnum(ClinicalStatus.RESOLVED.getValue())
            .setVerificationStatusEnum(VerificationStatus.REFUTED.getValue())
            .setTypeEnum(allergyIntoInfoDto.getTypeEnum()).setCategoryCode(allergyIntoInfoDto.getCategoryCode())
            .setCriticalityEnum(allergyIntoInfoDto.getCriticalityEnum()).setCode(allergyIntoInfoDto.getCode())
            .setPatientId(allergyIntoInfoDto.getPatientId()).setDescription(allergyIntoInfoDto.getDescription())
            .setSeverityEnum(allergyIntoInfoDto.getSeverityEnum())
            .setOnsetDateTime(allergyIntoInfoDto.getOnsetDateTime()).setCheckPractitionerId(practitionerId)
            .setLastReactionOccurrence(allergyIntoInfoDto.getLastReactionOccurrence())
            .setNote(allergyIntoInfoDto.getNote());

        boolean result = allergyIntoleranceService.saveOrUpdateAllergyIntolerance(allergyIntolerance);

        if (result) {
            return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"过敏与不耐受"}));
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

    }

    /**
     * 新增患者过敏与不耐受信息
     *
     * @param allergyIntoInfoDto 患者过敏与不耐受信息
     * @return
     */
    @Override
    public R<?> addAllergyIntoleranceInfo(AllergyIntoInfoDto allergyIntoInfoDto) {

        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
        // 获取当前登录账号的参与者id
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();

        allergyIntolerance.setId(allergyIntoInfoDto.getId()).setClinicalStatusEnum(ClinicalStatus.ACTIVE.getValue())
            .setVerificationStatusEnum(VerificationStatus.CONFIRMED.getValue())
            .setTypeEnum(allergyIntoInfoDto.getTypeEnum()).setCategoryCode(allergyIntoInfoDto.getCategoryCode())
            .setCriticalityEnum(allergyIntoInfoDto.getCriticalityEnum()).setCode(allergyIntoInfoDto.getCode())
            .setPatientId(allergyIntoInfoDto.getPatientId()).setDescription(allergyIntoInfoDto.getDescription())
            .setSeverityEnum(allergyIntoInfoDto.getSeverityEnum())
            .setOnsetDateTime(allergyIntoInfoDto.getOnsetDateTime()).setPractitionerId(practitionerId)
            .setRecordedDate(DateUtils.getNowDate())
            .setLastReactionOccurrence(allergyIntoInfoDto.getLastReactionOccurrence())
            .setNote(allergyIntoInfoDto.getNote());

        boolean result = allergyIntoleranceService.saveOrUpdateAllergyIntolerance(allergyIntolerance);

        if (result) {
            return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"过敏与不耐受"}));
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, null));
        }
    }

}
