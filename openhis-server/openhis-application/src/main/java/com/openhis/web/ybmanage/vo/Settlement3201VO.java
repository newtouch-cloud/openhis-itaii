/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 【3201】后台计算结果 DB映射实体
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
public class Settlement3201VO {
    /** 医疗费用总额 */
    private BigDecimal medFeeSumAmt;
    /** 基金支付总额 */
    private BigDecimal fundPaySumAmt;
    /** 个人账户支付总额 */
    private BigDecimal acctPay;
    /** 个人账户支付总额 */
    private BigDecimal acctGjPay;
    /** 定点医药机构结算笔数 */
    private Integer fixMedInsSetlCnt;
}
