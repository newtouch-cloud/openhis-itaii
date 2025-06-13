package com.openhis.web.doctorstation.mapper;

import com.openhis.web.doctorstation.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 医生站-电子处方 应用Mapper
 */
@Repository
public interface DoctorStationElepPrescriptionMapper {

    /**
     * 获取处方信息
     *
     * @param page 分页
     * @param patientId 患者id
     * @return 药品信息
     */
    Page<ElepPrescriptionInfoDto> selectElepPrescriptionInfo(@Param("page") Page<ElepPrescriptionInfoDto> page,@Param("patientId") Long patientId);
    /**
     * 获取处方信息
     *
     * @param prescriptionNo 处方号
     * @return 药品信息
     */
    Page<ElepMedicationInfoDto> selectMedicationInfo(@Param("page") Page<ElepPrescriptionInfoDto> page,@Param("prescriptionNo") String prescriptionNo);

    /**
     * 搜索更表需要信息
     *
     * @param encounterId 就诊id
     * @return 药品信息
     */
    ElepPrescriptionInfoParam selectSaveInfo(@Param("encounterId") Long encounterId);

}
