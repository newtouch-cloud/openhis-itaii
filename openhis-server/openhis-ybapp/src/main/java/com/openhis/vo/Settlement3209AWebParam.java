/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
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
    @JSONField(name = "clr_type")
    private String clrType;

    // 开始日期
    @JSONField(name = "begndate")
    private Date begndate;

    // 结束日期
    @JSONField(name = "enddate")
    private Date enddate;

    // 清算机构
    @JSONField(name = "clr_optins")
    private String clrOptins;
}
