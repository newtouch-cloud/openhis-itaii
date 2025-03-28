/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 处方信息
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class PrescriptionInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 患者基本信息 */
    private PrescriptionPatientInfoDto prescriptionPatientInfoDto;

    /** 处方药品信息 */
    List<PrescriptionMedicineInfoDto> prescriptionMedicineInfoDtoList;
}