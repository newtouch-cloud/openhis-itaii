/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 *【3209A】查询跨省三方对账未成功数据(吉林省)-输入参数
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FinancialSettlement3209AParam {

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
