package com.openhis.web.doctorstation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.openhis.web.doctorstation.dto.DiagnosisQueryDto;

/**
 * 医生站-中医 应用Mapper
 */
@Repository
public interface DoctorStationChineseMedicalAppMapper {

    /**
     * 查询中医就诊诊断信息
     *
     * @param encounterId 就诊id
     * @return 中医就诊诊断信息
     */
    List<DiagnosisQueryDto> getTcmEncounterDiagnosis(@Param("encounterId") Long encounterId);

}
