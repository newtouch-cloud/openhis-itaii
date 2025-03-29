/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.payment.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 支付详情
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Data
@Accessors(chain = true)
public class PaymentDetailDto {

    /** 付款类型 */
    private Integer targetEnum;

    /** 账户 */
    private Long accountId;

    /** 账户类型 */
    private Integer accountEnum;

    /** 支付类型 */
    private Integer payEnum;

    /** 支付类型等级 */
    private Integer payLevelEnum;

    /** 金额 */
    private BigDecimal amount;

    /** 找零 */
    private BigDecimal returnAmount;

    /** 交款 */
    private BigDecimal chargeAmount;

}
