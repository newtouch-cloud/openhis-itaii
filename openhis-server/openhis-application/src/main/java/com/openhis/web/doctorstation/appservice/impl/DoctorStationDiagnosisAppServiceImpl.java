package com.openhis.web.doctorstation.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.EncounterDiagnosis;
import com.openhis.administration.service.IEncounterDiagnosisService;
import com.openhis.clinical.domain.Condition;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.domain.DiagnosisBelongBinding;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.clinical.service.IConditionService;
import com.openhis.clinical.service.IDiagnosisBelongBindingService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.doctorstation.appservice.IDoctorStationDiagnosisAppService;
import com.openhis.web.doctorstation.dto.*;
import com.openhis.web.doctorstation.mapper.DoctorStationDiagnosisAppMapper;

/**
 * 医生站-诊断 应用实现类
 */
@Service
public class DoctorStationDiagnosisAppServiceImpl implements IDoctorStationDiagnosisAppService {

    @Resource
    IDiagnosisBelongBindingService iDiagnosisBelongBindingService;

    @Resource
    DoctorStationDiagnosisAppMapper doctorStationDiagnosisAppMapper;

    @Resource
    ConditionDefinitionMapper conditionDefinitionMapper;

    @Resource
    IConditionService iConditionService;

    @Resource
    IEncounterDiagnosisService iEncounterDiagnosisService;

    /**
     * 新增诊断归属绑定
     *
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    @Override
    public R<?> addDiagnosisBelongBinding(DiagnosisBelongBindingDto diagnosisBelongBindingDto) {
        // 如果绑定类型是个人,objectId 存储当前登录账号id
        if (BindingType.PERSONAL.getValue().equals(diagnosisBelongBindingDto.getBindingEnum())) {
            diagnosisBelongBindingDto.setObjectId(SecurityUtils.getLoginUser().getUserId());
        } else if (BindingType.DEFINITION.getValue().equals(diagnosisBelongBindingDto.getBindingEnum())) {
            diagnosisBelongBindingDto.setObjectId(SecurityUtils.getLoginUser().getOrgId());
        }
        DiagnosisBelongBinding diagnosisBelongBinding = new DiagnosisBelongBinding();
        BeanUtils.copyProperties(diagnosisBelongBindingDto, diagnosisBelongBinding);
        // 校验是否重复新增
        long count = iDiagnosisBelongBindingService.count(new LambdaQueryWrapper<DiagnosisBelongBinding>()
            .eq(DiagnosisBelongBinding::getObjectId, diagnosisBelongBindingDto.getObjectId())
            .eq(DiagnosisBelongBinding::getDefinitionId, diagnosisBelongBindingDto.getDefinitionId())
            .eq(DiagnosisBelongBinding::getBindingEnum, diagnosisBelongBindingDto.getBindingEnum()));
        if (count > 0L) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"绑定关系"}));
        }
        iDiagnosisBelongBindingService.save(diagnosisBelongBinding);
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"诊断归属绑定关系"}));
    }

    /**
     * 编辑诊断归属绑定
     *
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    @Override
    public R<?> updateDiagnosisBelongBinding(DiagnosisBelongBindingDto diagnosisBelongBindingDto) {
        // 如果绑定类型是个人,objectId 存储当前登录账号id
        if (BindingType.PERSONAL.getValue().equals(diagnosisBelongBindingDto.getBindingEnum())) {
            diagnosisBelongBindingDto.setObjectId(SecurityUtils.getLoginUser().getUserId());
        }
        DiagnosisBelongBinding diagnosisBelongBinding = new DiagnosisBelongBinding();
        BeanUtils.copyProperties(diagnosisBelongBindingDto, diagnosisBelongBinding);
        // 校验是否重复编辑
        long count = iDiagnosisBelongBindingService.count(new LambdaQueryWrapper<DiagnosisBelongBinding>()
            .eq(DiagnosisBelongBinding::getObjectId, diagnosisBelongBindingDto.getObjectId())
            .eq(DiagnosisBelongBinding::getDefinitionId, diagnosisBelongBindingDto.getDefinitionId())
            .eq(DiagnosisBelongBinding::getBindingEnum, diagnosisBelongBindingDto.getBindingEnum())
            .ne(DiagnosisBelongBinding::getId, diagnosisBelongBindingDto.getId()));
        if (count > 0L) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"绑定关系"}));
        }
        iDiagnosisBelongBindingService.updateById(diagnosisBelongBinding);
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊断归属绑定关系"}));
    }

    /**
     * 查询诊断归属绑定列表
     *
     * @param diagnosisBelongBindingDto 查询条件dto
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断归属绑定列表
     */
    @Override
    public R<?> getDiagnosisBelongBindingPage(DiagnosisBelongBindingDto diagnosisBelongBindingDto, String searchKey,
        Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<DiagnosisBelongBindingDto> queryWrapper = HisQueryUtils.buildQueryWrapper(
            diagnosisBelongBindingDto, searchKey, new HashSet<>(Arrays.asList("definition_name", "object_name")), null);
        IPage<DiagnosisBelongBindingDto> diagnosisBelongBindingPage =
            doctorStationDiagnosisAppMapper.getDiagnosisBelongBindingPage(new Page<>(pageNo, pageSize),
                BindingType.PERSONAL.getValue(), BindingType.DEFINITION.getValue(), queryWrapper);
        diagnosisBelongBindingPage.getRecords().forEach(e -> {
            // 诊断绑定类型
            e.setBindingEnum_enumText(EnumUtils.getInfoByValue(BindingType.class, e.getBindingEnum()));
        });
        return R.ok(diagnosisBelongBindingPage);
    }

    /**
     * 删除诊断归属绑定
     *
     * @param id ID
     * @return 结果
     */
    @Override
    public R<?> delDiagnosisBelongBinding(Long id) {
        boolean res = iDiagnosisBelongBindingService.removeById(id);
        return res ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"诊断归属绑定"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
    }

    /**
     * 查询诊断信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断信息
     */
    @Override
    public Page<ConditionDefinitionMetadata> getConditionDefinitionMetadataSearchKey(String searchKey, Integer pageNo,
        Integer pageSize) {
        // 构建查询条件
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        // 查询状态是有效的
        conditionDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
        QueryWrapper<ConditionDefinition> queryWrapper = HisQueryUtils.buildQueryWrapper(conditionDefinition, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), null);
        // 设置排序
        queryWrapper.orderByDesc("update_time");
        // 诊断信息
        Page<ConditionDefinitionMetadata> conditionDefinitionMetadataPage = HisPageUtils
            .selectPage(conditionDefinitionMapper, queryWrapper, pageNo, pageSize, ConditionDefinitionMetadata.class);
        this.handleConditionDefinitionMetadata(conditionDefinitionMetadataPage.getRecords());
        return conditionDefinitionMetadataPage;
    }

    /**
     * 医生保存诊断
     *
     * @param saveDiagnosisParam 诊断信息
     * @return 结果
     */
    @Override
    public R<?> saveDoctorDiagnosis(SaveDiagnosisParam saveDiagnosisParam) {
        // 患者id
        Long patientId = saveDiagnosisParam.getPatientId();
        // 就诊ID
        Long encounterId = saveDiagnosisParam.getEncounterId();
        // 诊断定义集合
        List<SaveDiagnosisChildParam> diagnosisChildList = saveDiagnosisParam.getDiagnosisChildList();
        // 先删除再保存
        iEncounterDiagnosisService.deleteEncounterDiagnosisInfos(encounterId);
        // 保存诊断管理
        Condition condition;
        for (SaveDiagnosisChildParam saveDiagnosisChildParam : diagnosisChildList) {
            condition = new Condition();
            condition.setVerificationStatusEnum(saveDiagnosisChildParam.getVerificationStatusEnum());
            condition.setPatientId(patientId);
            condition.setDefinitionId(saveDiagnosisChildParam.getDefinitionId());
            condition.setYbNo(saveDiagnosisChildParam.getYbNo());
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
            iEncounterDiagnosisService.save(encounterDiagnosis);
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊断"}));
    }

    /**
     * 查询诊断定义业务分类数据
     *
     * @param patientId 患者id
     * @return 诊断定义业务分类数据
     */
    @Override
    public R<?> getConditionDefinitionBusinessClass(Long patientId) {
        // 诊断定义业务分类信息
        ConditionDefinitionBusinessClass conditionDefinitionBusinessClass = new ConditionDefinitionBusinessClass();
        // 病人历史诊断
        List<ConditionDefinitionMetadata> patientHistoryList =
            doctorStationDiagnosisAppMapper.getPatientHistoryList(PublicationStatus.ACTIVE.getValue(), patientId);
        this.handleConditionDefinitionMetadata(patientHistoryList);
        conditionDefinitionBusinessClass.setPatientHistoryList(patientHistoryList);
        // 医生常用诊断
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId(); // 当前参与者ID
        List<ConditionDefinitionMetadata> doctorCommonUseList =
            doctorStationDiagnosisAppMapper.getDoctorCommonUseList(PublicationStatus.ACTIVE.getValue(), practitionerId);
        this.handleConditionDefinitionMetadata(doctorCommonUseList);
        conditionDefinitionBusinessClass.setDoctorCommonUseList(doctorCommonUseList);
        // 用户个人诊断
        Long userId = SecurityUtils.getLoginUser().getUserId();
        List<ConditionDefinitionMetadata> userPersonalList = doctorStationDiagnosisAppMapper
            .getUserPersonalList(PublicationStatus.ACTIVE.getValue(), BindingType.PERSONAL.getValue(), userId);
        this.handleConditionDefinitionMetadata(userPersonalList);
        conditionDefinitionBusinessClass.setUserPersonalList(userPersonalList);
        // 科室诊断
        Long currentUserOrganizationId = SecurityUtils.getLoginUser().getOrgId();
        List<ConditionDefinitionMetadata> organizationList = doctorStationDiagnosisAppMapper.getOrganizationList(
            PublicationStatus.ACTIVE.getValue(), BindingType.DEFINITION.getValue(), currentUserOrganizationId);
        this.handleConditionDefinitionMetadata(organizationList);
        conditionDefinitionBusinessClass.setOrganizationList(organizationList);
        return R.ok(conditionDefinitionBusinessClass);
    }

    /**
     * 查询就诊诊断信息
     *
     * @param encounterId 就诊id
     * @return 就诊诊断信息
     */
    @Override
    public R<?> getEncounterDiagnosis(Long encounterId) {
        List<DiagnosisQueryDto> encounterDiagnosis = doctorStationDiagnosisAppMapper.getEncounterDiagnosis(encounterId);
        for (DiagnosisQueryDto diagnosis : encounterDiagnosis) {
            // 中医诊断/西医诊断
//            if (ConditionDefinitionSource.TRADITIONAL_CHINESE_MEDICINE_DIAGNOSIS.getValue()
//                .equals(diagnosis.getSourceEnum())
//                || ConditionDefinitionSource.TRADITIONAL_CHINESE_MEDICINE_SYNDROME_CATALOG.getValue()
//                    .equals(diagnosis.getSourceEnum())) {
//                diagnosis.setTypeName(CommonConstants.BusinessName.TCM_DIAGNOSIS);
//            } else {
//                diagnosis.setTypeName(CommonConstants.BusinessName.WESTERN_MEDICINE_DIAGNOSIS);
//            }
            // 验证状态
            diagnosis.setVerificationStatusEnum_enumText(
                EnumUtils.getInfoByValue(ConditionVerificationStatus.class, diagnosis.getVerificationStatusEnum()));
        }
        return R.ok(encounterDiagnosis);
    }

    /**
     * 删除就诊诊断信息
     *
     * @param conditionId 诊断ID
     * @return 结果
     */
    @Override
    public R<?> delEncounterDiagnosis(Long conditionId) {
        iConditionService.removeById(conditionId);
        iEncounterDiagnosisService
            .remove(new LambdaQueryWrapper<EncounterDiagnosis>().eq(EncounterDiagnosis::getConditionId, conditionId));
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"就诊诊断信息"}));
    }

    /**
     * 处理诊断定义元数据
     *
     * @param conditionDefinitionMetadataList 诊断定义元数据集合
     */
    private void handleConditionDefinitionMetadata(List<ConditionDefinitionMetadata> conditionDefinitionMetadataList) {
        conditionDefinitionMetadataList.forEach(e -> {
            // 所属分类
            e.setSourceEnum_enumText(EnumUtils.getInfoByValue(ConditionDefinitionSource.class, e.getSourceEnum()));
            // 中医诊断/西医诊断
            if (ConditionDefinitionSource.TRADITIONAL_CHINESE_MEDICINE_DIAGNOSIS.getValue().equals(e.getSourceEnum())
                || ConditionDefinitionSource.TRADITIONAL_CHINESE_MEDICINE_SYNDROME_CATALOG.getValue()
                    .equals(e.getSourceEnum())) {
                e.setTypeName(CommonConstants.BusinessName.TCM_DIAGNOSIS);
            } else {
                e.setTypeName(CommonConstants.BusinessName.WESTERN_MEDICINE_DIAGNOSIS);
            }
            // 医保标记
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbFlag()));
            // 医保对码标记
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbMatchFlag()));
        });
    }

}
