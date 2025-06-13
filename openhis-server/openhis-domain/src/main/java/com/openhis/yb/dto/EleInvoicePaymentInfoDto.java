/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 付款信息
 *
 * @author yuxj
 * @date 2025-04-22
 */
@Data
@Accessors(chain = true)
public class EleInvoicePaymentInfoDto {
    // 付款状态
    private Integer paymentStatus;

    // 发票id
    private Long invoiceId;

    // 发票状态
    private Integer invoiceStatus;

    // 结算id
    private Long paymentId;

    // 支付的业务标识符(用于显示,支付流水号)
    private String busNo;

    // 收费员
    private String payee;

    // 开票总金额
    private BigDecimal totalAmt;

    // 个人现金支付金额
    private BigDecimal rmbCashPayAmount;
    // 个人现金支付金额(微信)
    private BigDecimal wxPayAmount;
    // 个人现金支付金额(支付宝)
    private BigDecimal aliPayAmount;
    // 个人现金支付金额(银联)
    private BigDecimal debitPayAmount;
    // 个人医保账户支付
    private BigDecimal zhPayAmount;
    // 基金支付总额
    private BigDecimal ybFundFayAmount;
    // 其他（如医院负担金额）
    private BigDecimal otherPayAmount;
    // 账户共济支付金额
    private BigDecimal aelfYbZhGjValue;

    // 付款账单集合
    private String chargeItemIds;
}
