/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.pharmacymanage.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WesternMedicineDispenseMapper {

    /**
     * 就诊病人列表分页查询
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 就诊病人列表
     */
    Page<EncounterInfoPageDto> selectEncounterInfoListPage(@Param("page") Page<EncounterInfoPageDto> page,
                                                           @Param(Constants.WRAPPER) QueryWrapper<EncounterInfoSearchParam> queryWrapper);

    /**
     * 患者基本信息查询
     * @param encounterId 就诊号
     * @return 患者基本信息
     */
    PrescriptionPatientInfoDto selectPrescriptionPatientInfo(@Param("encounterId") Long encounterId);


    /**
     * 处方单查询
     * @param encounterId 就诊号
     * @return 处方单列表
     */
    List<PrescriptionMedicineInfoDto> selectPrescriptionMedicineInfoList(@Param("encounterId") Long encounterId);

    /**
     * 待发药和库存信息查询
     * @param  prescriptionNo 处方号
     * @return 待发药信息
     */
    List<DispenseInventoryDto> selectDispenseInventoryInfoByPrescriptionNo(@Param("prescriptionNo") String prescriptionNo);
}
