/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 【3201】前台入参
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
public class Settlement3201WebParam {

    /** 险种 */
    @NotNull
    private String insuType;
    /** 清算类别 */
    @NotNull
    private String clrType;//住院或门诊
    /** 合同id */
    @NotNull
    private Long contractId;
    /** 医院id */
    @NotNull
    private Long orgId;
    /** 开始时间 */
    @NotNull
    private String stmtBegnDate;
    /** 结束时间 */
    @NotNull
    private String stmtEndDate;
}
