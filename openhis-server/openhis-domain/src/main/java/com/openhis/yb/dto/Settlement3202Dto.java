/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 【3201】后台计算结果 DB映射实体
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
public class Settlement3202Dto {
    /** 医疗费用总额 */

    private BigDecimal medFeeSumAmt;
    /** 基金支付总额 */

    private BigDecimal fundPaySumAmt;
    /** 个人账户支付总额 */

    private BigDecimal acctPay;
    /** 个人账户支付总额 */

    private BigDecimal acctGjPay;
    /** 现金支付总额 */

    private BigDecimal selfPayCash;
    /** 微信支付总额 */

    private BigDecimal selfPayVX;
    /** 阿里支付总额 */

    private BigDecimal selfPayALI;
    /** 银行卡支付总额 */

    private BigDecimal selfPayUNION;
    /** 定点医药机构结算笔数 */

    private Integer fixMedInsSetlCnt;
}
