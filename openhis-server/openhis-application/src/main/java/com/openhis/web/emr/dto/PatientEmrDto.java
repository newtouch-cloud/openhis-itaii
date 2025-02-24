/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.emr.dto;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 病人电子病历信息
 *
 * @author ZhangYC
 * @date 2025-02-22
 */
@Data
@Accessors(chain = true)
public class PatientEmrDto implements Serializable {

    /** 患者ID */
    @NotNull
    private Long patientId;

    /** 就诊ID */
    @NotNull
    private Long encounterId;

    /** 病历信息 */
    private JSONObject contextJson;

    /** 病历状态 */
    private String emrStatus;

    /** 记录人 */
    @NotNull
    private Long recordId;


}
