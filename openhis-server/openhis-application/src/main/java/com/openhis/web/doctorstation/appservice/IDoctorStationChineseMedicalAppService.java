package com.openhis.web.doctorstation.appservice;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.AdviceSaveParam;
import com.openhis.web.doctorstation.dto.ConditionDefinitionMetadata;
import com.openhis.web.doctorstation.dto.SaveDiagnosisParam;

/**
 * 医生站-中医 应用Service
 */
public interface IDoctorStationChineseMedicalAppService {

    /**
     * 查询中医诊断数据
     *
     * @param searchKey 关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 中医诊断数据
     */
    Page<ConditionDefinitionMetadata> getConditionInfo(String searchKey, Integer pageNo, Integer pageSize);

    /**
     * 查询中医证候数据
     *
     * @param searchKey 关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 中医证候数据
     */
    Page<ConditionDefinitionMetadata> getSyndromeInfo(String searchKey, Integer pageNo, Integer pageSize);

    /**
     * 保存中医诊断
     *
     * @param saveDiagnosisParam 诊断信息
     * @return 结果
     */
    R<?> saveTcmDiagnosis(SaveDiagnosisParam saveDiagnosisParam);

    /**
     * 查询中医就诊诊断信息
     *
     * @param encounterId 就诊id
     * @return 中医就诊诊断信息
     */
    R<?> getTcmEncounterDiagnosis(Long encounterId);

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
    IPage<AdviceBaseDto> getTcmAdviceBaseInfo(AdviceBaseDto adviceBaseDto, String searchKey, Long locationId,
        List<Long> adviceDefinitionIdParamList, Long organizationId, Integer pageNo, Integer pageSize,
        Integer pricingFlag);

    /**
     * 门诊保存(签发)中医医嘱
     *
     * @param adviceSaveParam 医嘱表单信息
     * @param adviceOpType 保存或签发
     * @return 结果
     */
    R<?> saveOrSignTcmAdvice(AdviceSaveParam adviceSaveParam, String adviceOpType);

}
