package com.openhis.web.doctorstation.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.domain.EncounterDiagnosis;
import com.openhis.administration.service.IEncounterDiagnosisService;
import com.openhis.clinical.domain.Condition;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.clinical.service.IConditionService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;
import com.openhis.web.doctorstation.appservice.IDoctorStationChineseMedicalAppService;
import com.openhis.web.doctorstation.dto.*;
import com.openhis.web.doctorstation.mapper.DoctorStationChineseMedicalAppMapper;
import com.openhis.web.doctorstation.utils.AdviceUtils;

/**
 * 医生站-中医 应用实现类
 */
@Service
public class DoctorStationChineseMedicalAppServiceImpl implements IDoctorStationChineseMedicalAppService {

    @Resource
    DoctorStationChineseMedicalAppMapper doctorStationChineseMedicalAppMapper;

    @Resource
    ConditionDefinitionMapper conditionDefinitionMapper;

    @Resource
    IConditionService iConditionService;

    @Resource
    IEncounterDiagnosisService iEncounterDiagnosisService;

    @Resource
    IDoctorStationAdviceAppService iDoctorStationAdviceAppService;

    @Resource
    AssignSeqUtil assignSeqUtil;

    @Resource
    AdviceUtils adviceUtils;

    /**
     * 查询中医诊断数据
     *
     * @param searchKey 关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 中医诊断数据
     */
    @Override
    public Page<ConditionDefinitionMetadata> getConditionInfo(String searchKey, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        // 查询状态是有效的
        conditionDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
        // 中医诊断
        conditionDefinition.setSourceEnum(ConditionDefinitionSource.TRADITIONAL_CHINESE_MEDICINE_DIAGNOSIS.getValue());
        QueryWrapper<ConditionDefinition> queryWrapper = HisQueryUtils.buildQueryWrapper(conditionDefinition, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), null);
        // 设置排序
        queryWrapper.orderByDesc("update_time");
        // 诊断信息
        Page<ConditionDefinitionMetadata> conditionDefinitionMetadataPage = HisPageUtils
            .selectPage(conditionDefinitionMapper, queryWrapper, pageNo, pageSize, ConditionDefinitionMetadata.class);
        conditionDefinitionMetadataPage.getRecords().forEach(e -> {
            // 所属分类
            e.setSourceEnum_enumText(EnumUtils.getInfoByValue(ConditionDefinitionSource.class, e.getSourceEnum()));
            // 中医诊断
            e.setTypeName(CommonConstants.BusinessName.TCM_DIAGNOSIS);
            // 医保标记
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbFlag()));
            // 医保对码标记
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbMatchFlag()));
        });
        return conditionDefinitionMetadataPage;
    }

    /**
     * 查询中医证候数据
     *
     * @param searchKey 关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 中医证候数据
     */
    @Override
    public Page<ConditionDefinitionMetadata> getSyndromeInfo(String searchKey, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        // 查询状态是有效的
        conditionDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
        // 中医证候
        conditionDefinition
            .setSourceEnum(ConditionDefinitionSource.TRADITIONAL_CHINESE_MEDICINE_SYNDROME_CATALOG.getValue());
        QueryWrapper<ConditionDefinition> queryWrapper = HisQueryUtils.buildQueryWrapper(conditionDefinition, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), null);
        // 设置排序
        queryWrapper.orderByDesc("update_time");
        // 诊断信息
        Page<ConditionDefinitionMetadata> conditionDefinitionMetadataPage = HisPageUtils
            .selectPage(conditionDefinitionMapper, queryWrapper, pageNo, pageSize, ConditionDefinitionMetadata.class);
        conditionDefinitionMetadataPage.getRecords().forEach(e -> {
            // 所属分类
            e.setSourceEnum_enumText(EnumUtils.getInfoByValue(ConditionDefinitionSource.class, e.getSourceEnum()));
            // 中医证候
            e.setTypeName(CommonConstants.BusinessName.TCM_SYNDROME_CATALOG);
            // 医保标记
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbFlag()));
            // 医保对码标记
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbMatchFlag()));
        });
        return conditionDefinitionMetadataPage;
    }

    /**
     * 保存中医诊断
     *
     * @param saveDiagnosisParam 诊断信息
     * @return 结果
     */
    @Override
    public R<?> saveTcmDiagnosis(SaveDiagnosisParam saveDiagnosisParam) {
        // 患者id
        Long patientId = saveDiagnosisParam.getPatientId();
        // 就诊ID
        Long encounterId = saveDiagnosisParam.getEncounterId();
        // 诊断定义集合
        List<SaveDiagnosisChildParam> diagnosisChildList = saveDiagnosisParam.getDiagnosisChildList();
        // 先删除再保存 (中医)
        iEncounterDiagnosisService.deleteEncounterDiagnosisInfos(encounterId);
        // 保存诊断管理
        Condition condition;
        for (SaveDiagnosisChildParam saveDiagnosisChildParam : diagnosisChildList) {
            condition = new Condition();
            condition.setVerificationStatusEnum(saveDiagnosisChildParam.getVerificationStatusEnum());
            condition.setPatientId(patientId);
            condition.setDefinitionId(saveDiagnosisChildParam.getDefinitionId());
            condition.setYbNo(saveDiagnosisChildParam.getYbNo());
            condition.setTcmFlag(Whether.YES.getValue());// 中医标识
            // 返回诊断id
            Long conditionId = iConditionService.saveConditionByDoctor(condition);
            saveDiagnosisChildParam.setConditionId(conditionId);
        }
        // 保存就诊诊断
        EncounterDiagnosis encounterDiagnosis;
        for (SaveDiagnosisChildParam saveDiagnosisChildParam : diagnosisChildList) {
            encounterDiagnosis = new EncounterDiagnosis();
            encounterDiagnosis.setEncounterId(encounterId);
            encounterDiagnosis.setConditionId(saveDiagnosisChildParam.getConditionId());
            encounterDiagnosis.setMaindiseFlag(saveDiagnosisChildParam.getMaindiseFlag());
            encounterDiagnosis.setDiagSrtNo(saveDiagnosisChildParam.getDiagSrtNo()); // 排序号
            encounterDiagnosis.setMedTypeCode(saveDiagnosisChildParam.getMedTypeCode());// 医疗类型
            encounterDiagnosis.setTcmFlag(Whether.YES.getValue());// 中医标识
            encounterDiagnosis.setSyndromeGroupNo(saveDiagnosisChildParam.getSyndromeGroupNo());// 中医证候组号
            iEncounterDiagnosisService.save(encounterDiagnosis);
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"中医诊断"}));
    }

    /**
     * 查询中医就诊诊断信息
     *
     * @param encounterId 就诊id
     * @return 中医就诊诊断信息
     */
    @Override
    public R<?> getTcmEncounterDiagnosis(Long encounterId) {
        HashMap<String, Object> resultMap = new HashMap<>();
        // 中医就诊诊断信息
        List<DiagnosisQueryDto> tcmEncounterDiagnosis =
            doctorStationChineseMedicalAppMapper.getTcmEncounterDiagnosis(encounterId);
        // 病
        List<DiagnosisQueryDto> illness = tcmEncounterDiagnosis.stream().filter(
            e -> ConditionDefinitionSource.TRADITIONAL_CHINESE_MEDICINE_DIAGNOSIS.getValue().equals(e.getSourceEnum()))
            .collect(Collectors.toList());
        // 症
        List<DiagnosisQueryDto> symptom = tcmEncounterDiagnosis.stream()
            .filter(e -> ConditionDefinitionSource.TRADITIONAL_CHINESE_MEDICINE_SYNDROME_CATALOG.getValue()
                .equals(e.getSourceEnum()))
            .collect(Collectors.toList());
        resultMap.put("illness", illness);
        resultMap.put("symptom", symptom);
        return R.ok(resultMap);
    }

    /**
     * 查询中医医嘱信息
     *
     * @param adviceBaseDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param locationId 药房id
     * @param adviceDefinitionIdParamList 医嘱定义id参数集合
     * @param organizationId 患者挂号对应的科室id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param pricingFlag 划价标记
     * @return 中医医嘱信息
     */
    @Override
    public IPage<AdviceBaseDto> getTcmAdviceBaseInfo(AdviceBaseDto adviceBaseDto, String searchKey, Long locationId,
        List<Long> adviceDefinitionIdParamList, Long organizationId, Integer pageNo, Integer pageSize,
        Integer pricingFlag) {
        adviceBaseDto.setAdviceType(1); // 医嘱类型为药品
        adviceBaseDto.setCategoryCode(medCategoryCode.CHINESE_HERBAL_MEDICINE.getValue());// 中草药
        return iDoctorStationAdviceAppService.getAdviceBaseInfo(adviceBaseDto, searchKey, locationId,
            adviceDefinitionIdParamList, organizationId, pageNo, pageSize, pricingFlag);
    }

    /**
     * 门诊保存(签发)中医医嘱
     *
     * @param adviceSaveParam 医嘱表单信息
     * @param adviceOpType 保存或签发
     * @return 结果
     */
    @Override
    public R<?> saveOrSignTcmAdvice(AdviceSaveParam adviceSaveParam, String adviceOpType) {
        // 医嘱分类信息 (中草药)
        List<AdviceSaveDto> medicineList = adviceSaveParam.getAdviceSaveList();
        // 保存操作
        boolean is_save = AdviceOpType.SAVE_ADVICE.getCode().equals(adviceOpType);
        // 签发操作
        boolean is_sign = AdviceOpType.SIGN_ADVICE.getCode().equals(adviceOpType);
        // 当前时间
        Date curDate = new Date();
        // 患者挂号对应的科室id
        Long organizationId = adviceSaveParam.getOrganizationId();
        // 保存时,校验库存
        if (is_save) {
            List<AdviceSaveDto> needCheckList = medicineList.stream()
                .filter(e -> !DbOpType.DELETE.getCode().equals(e.getDbOpType())).collect(Collectors.toList());
            // 校验库存
            String tipRes = adviceUtils.checkInventory(needCheckList);
            if (tipRes != null) {
                return R.fail(null, tipRes);
            }
        }
        // 药品请求
        MedicationRequest medicationRequest;
        // 费用项
        ChargeItem chargeItem;
        // 是否代煎
        Integer sufferingFlag = adviceSaveParam.getSufferingFlag();
        for (AdviceSaveDto adviceSaveDto : medicineList) {
            // 中药付数
            Integer chineseHerbsDoseQuantity = adviceSaveDto.getChineseHerbsDoseQuantity();
            medicationRequest = new MedicationRequest();
            medicationRequest.setTcmFlag(Whether.YES.getValue());// 中医标识
            medicationRequest.setId(adviceSaveDto.getRequestId()); // 主键id
            medicationRequest.setStatusEnum(is_save ? RequestStatus.DRAFT.getValue() : RequestStatus.ACTIVE.getValue()); // 请求状态
            // 只有保存时才处理的字段属性
            if (is_save) {
                medicationRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.MEDICATION_RES_NO.getPrefix(), 10));
                medicationRequest.setChineseHerbsDoseQuantity(chineseHerbsDoseQuantity); // 中药付数
                medicationRequest.setSufferingFlag(sufferingFlag); // 代煎标识
                medicationRequest.setQuantity(adviceSaveDto.getQuantity() * chineseHerbsDoseQuantity); // 请求数量

            }

            // 代煎,生成中药代煎的request,及对应的账单
            if (Whether.YES.getValue().equals(sufferingFlag)) {

            }
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"门诊中医医嘱"}));
    }

}
