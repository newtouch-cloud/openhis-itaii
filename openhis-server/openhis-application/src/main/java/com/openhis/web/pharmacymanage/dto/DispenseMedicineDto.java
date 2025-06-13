/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发药信息
 *
 * @author zwh
 * @date 2025-05-23
 */
@Data
@Accessors(chain = true)
public class DispenseMedicineDto {
    /** 药品请求ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requestId;

    /** 配药人 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long preparerId;

    /** 药品发放ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dispenseId;

    /** 退药追溯码 */
    private String traceNo;

    /** 处方号 */
    private String prescriptionNo;

    /** 未发药原因 */
    private Integer notPerformedReasonEnum;
}
