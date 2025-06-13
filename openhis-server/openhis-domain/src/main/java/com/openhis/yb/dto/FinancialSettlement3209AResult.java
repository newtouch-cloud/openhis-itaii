/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3209A】查询跨省三方对账未成功数据(吉林省)-输出参数]
 *
 * @author SunJQ
 * @date 2025-04-21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FinancialSettlement3209AResult {
    // 结算ID
    @JSONField(name = "setlId")
    private String setlId;

    // 参保地区划
    @JSONField(name = "insuAdmdvs")
    private String insuAdmdvs;
}
