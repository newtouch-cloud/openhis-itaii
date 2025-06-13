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
public class Settlement3202VO {
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

    private BigDecimal selfPayVx;
    /** 阿里支付总额 */

    private BigDecimal selfPayAli;
    /** 银行卡支付总额 */

    private BigDecimal selfPayUnion;
    /** 定点医药机构结算笔数 */

    private Integer fixMedInsSetlCnt;
}
