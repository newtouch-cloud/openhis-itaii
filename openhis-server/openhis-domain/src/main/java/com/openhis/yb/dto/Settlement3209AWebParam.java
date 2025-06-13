/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import lombok.Data;

import java.util.Date;

/**
 * [3209A]前台传参
 *
 * @author SunJQ
 * @date 2025-04-29
 */
@Data
public class Settlement3209AWebParam {

    // 清算类别
    private String clrType;

    // 开始日期
    private Date begndate;

    // 结束日期
    private Date enddate;

    // 清算机构
    private String clrOptins;
}
