/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.administration.domain.Practitioner;
import com.openhis.web.pharmacymanage.dto.DispenseInventoryDto;
import com.openhis.web.pharmacymanage.dto.EncounterInfoPageDto;
import com.openhis.web.pharmacymanage.dto.PrescriptionMedicineInfoDto;
import com.openhis.web.pharmacymanage.dto.PrescriptionPatientInfoDto;

@Repository
public interface WesternMedicineDispenseMapper {

    /**
     * 就诊病人列表分页查询
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param statusEnum 发药状态
     * @param inProgress 发药状态:待发药
     * @param completed 发药状态:已发药
     * @param preparation 发药状态:待配药
     * @param prepared 发药状态:已配药
     * @return 就诊病人列表
     */
    Page<EncounterInfoPageDto> selectEncounterInfoListPage(@Param("page") Page<EncounterInfoPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<EncounterInfoPageDto> queryWrapper,
        @Param("statusEnum") Integer statusEnum, @Param("inProgress") Integer inProgress,
        @Param("completed") Integer completed, @Param("preparation") Integer preparation,
        @Param("prepared") Integer prepared);

    /**
     * 患者基本信息查询
     *
     * @param encounterId 就诊号
     * @return 患者基本信息
     */
    PrescriptionPatientInfoDto selectPrescriptionPatientInfo(@Param("encounterId") Long encounterId);

    /**
     * 处方单查询
     *
     * @param encounterId 就诊号
     * @param inProgress 发药状态:待发药
     * @param completed 发药状态:已发药
     * @param preparation 发药状态:待配药
     * @param prepared 发药状态:已配药
     * @param dispenseStatus 发药状态
     * @return 处方单列表
     */
    List<PrescriptionMedicineInfoDto> selectPrescriptionMedicineInfoList(@Param("encounterId") Long encounterId,
        @Param("inProgress") Integer inProgress, @Param("completed") Integer completed,
        @Param("preparation") Integer preparation, @Param("prepared") Integer prepared,
        @Param("dispenseStatus") Integer dispenseStatus);

    /**
     * 待发药和库存信息查询
     *
     * @param prescriptionNo 处方号
     * @return 待发药信息
     */
    List<DispenseInventoryDto> selectDispenseInventoryInfoByPrescriptionNo(
        @Param("prescriptionNo") String prescriptionNo,
        @Param("medMedicationDefinition") String medMedicationDefinition);

    /**
     * 获取配药人下拉选列表
     *
     * @param pharmacist 参与者类型：药师
     * @return 配药人下拉选列表
     */
    List<Practitioner> getPreparerDoctorList(@Param("pharmacist") String pharmacist);
}
