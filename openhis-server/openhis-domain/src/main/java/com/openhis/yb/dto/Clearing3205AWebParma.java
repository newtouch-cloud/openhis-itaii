/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 【3205A】前端入参
 *
 * @author SunJQ
 * @date 2025-04-17
 */
@Data
public class Clearing3205AWebParma {
    // 清算机构
    @NotNull
    private String clrOptins;

    // 清算状态
    private String clrStas;

    // 清算月份 例如：202310
    private String clrYm;
}
