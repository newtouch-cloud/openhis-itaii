/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3205A】清算申请状态查询（吉林省）
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Clearing3205AParma extends Financial3203AParam {

    // 清算机构
    @JSONField(name = "clr_optins")
    private String clrOptins;

    // 清算状态
    @JSONField(name = "clr_stas")
    private String clrStas;

    // 清算月份
    @JSONField(name = "clr_ym")
    private String clrYm;
}
