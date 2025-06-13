/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.vo;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 【3203】后台计算结果  DB映射实体
 *
 * @author SunJQ
 * @date 2025-04-29
 */
@Data
public class Financial3203VO {
    // 清算人次
    private Long psntime;
    // 医保认可费用总额（数值型，16位含2位小数，必填）
    private BigDecimal medSumfee;
    // 现金支付金额（数值型，16位含2位小数，必填）
    private BigDecimal cashPayamt;
    // 个人账户支出（数值型，16位含2位小数，必填）
    private BigDecimal acctPay;
    /** 医疗费用总额 */
    private BigDecimal medFeeSumAmt;
    /** 基金支付总额 */
    private BigDecimal fundPaySumAmt;
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
}
