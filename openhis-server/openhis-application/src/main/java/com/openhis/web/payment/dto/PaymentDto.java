/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.payment.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 付款入参
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Data
@Accessors(chain = true)
public class PaymentDto {

    /**
     * id
     */
    private Long id;

    /** 发起支付的工作流程类别 */
    @NotNull
    private Integer kindEnum;
    private String kindEnum_enumText;

    /** 付款类别 */
    @NotNull
    private Integer paymentEnum;
    private String paymentEnum_enumText;

    /** 收款员 */
    private Long entererId;

    /** 支付的患者ID */
    @NotNull
    private Long patientId;

    /** 应收金额 */
    @NotNull
    private BigDecimal tenderedAmount;

    /** 找零金额 */
    @NotNull
    private BigDecimal returnedAmount;

    /** 付款总额 */
    @NotNull
    private BigDecimal displayAmount;

    /** 合同编码 */
    @NotNull
    private String contractNo;

    /** 就诊ID */
    @NotBlank
    private Long encounterId;

    /** 收费项 */
    @NotBlank
    private String chargeItemIds;

    /** 支付详细 */
    @NotEmpty
    private List<PaymentDetailDto> paymentDetails;
}
