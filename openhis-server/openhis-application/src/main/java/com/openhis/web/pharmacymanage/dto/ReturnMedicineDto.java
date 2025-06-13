/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退药信息
 *
 * @author zwh
 * @date 2025-05-07
 */
@Data
@Accessors(chain = true)
public class ReturnMedicineDto {

    /** 药品请求ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requestId;

    /** 药品发放ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dispenseId;

    /** 业务表名 */
    private String tableName;

    /** 退药追溯码 */
    private String traceNo;
}
