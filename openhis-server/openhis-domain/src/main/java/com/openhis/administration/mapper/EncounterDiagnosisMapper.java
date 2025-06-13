package com.openhis.administration.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.administration.domain.EncounterDiagnosis;

/**
 * 就诊诊断管理Mapper接口
 *
 * @author system
 * @date 2025-02-20
 */
@Repository
public interface EncounterDiagnosisMapper extends BaseMapper<EncounterDiagnosis> {

    /**
     * 删除就诊信息
     *
     * @param encounterId 就诊id
     */
    void deleteByEncounterId(@Param("encounterId") Long encounterId);

    /**
     * 删除中医就诊信息
     *
     * @param encounterId 就诊id
     */
    void deleteTcmByEncounterId(@Param("encounterId") Long encounterId);

}