/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 处方信息
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class PrescriptionInfoDto {

    /** 患者基本信息 */
    private PrescriptionPatientInfoDto prescriptionPatientInfoDto;

    /** 处方药品信息 */
    private List<PrescriptionMedicineInfoDto> prescriptionMedicineInfoDtoList;
}
