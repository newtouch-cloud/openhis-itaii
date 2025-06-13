/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.outpatientmanage.dto;

import java.util.List;

import com.openhis.web.pharmacymanage.dto.PrescriptionMedicineInfoDto;
import com.openhis.web.pharmacymanage.dto.PrescriptionPatientInfoDto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 处方信息
 *
 * @author yuxj
 * @date 2025-04-11
 */
@Data
@Accessors(chain = true)
public class OutpatientDisposalInfoDto {

    /** 患者基本信息 */
    private OutpatientDisposalPatientInfoDto outpatientDisposalPatientInfoDto;

    /** 诊疗信息 */
    List<OutpatientDisposalActivityInfoDto> outpatientDisposalActivityInfoDtoList;
}
