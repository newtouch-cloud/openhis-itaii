package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.EncounterDiagnosis;

import java.util.List;

/**
 * 就诊诊断管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IEncounterDiagnosisService extends IService<EncounterDiagnosis> {

    /**
     * 删除就诊信息
     * 
     * @param encounterId 就诊id
     */
    void deleteEncounterDiagnosisInfos(Long encounterId);

    /**
     * 删除中医诊断
     *
     * @param encounterId 就诊id
     */
    void deleteTcmEncounterDiagnosisInfos(Long encounterId);

    /**
     * 查询 med_type 类型
     * @param encounterId
     * @return
     */
    List<String> getMetTypeList(Long encounterId);

    /**
     * 查询 EncounterDiagnosis
     * @param encounterId
     * @return
     */
    List<EncounterDiagnosis> getDiagnosisList(Long encounterId);

    /**
     * 查询 EncounterDiagnosis
     * @param diaIdList
     * @return
     */
    List<EncounterDiagnosis> getDiagnosisList(List<Long> diaIdList);

}