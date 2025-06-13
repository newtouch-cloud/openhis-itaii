/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

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

    /** 收款员 */
    private Long entererId;

    /** 支付的患者ID */
    @NotNull
    private Long patientId;

    /** 应收金额 */
    private BigDecimal tenderedAmount;

    /** 找零金额 */
    private BigDecimal returnedAmount;

    /** 付款总额 */
    private BigDecimal displayAmount;

    /** 就诊ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 收费项 */
    private List<Long> chargeItemIds;//收费时传收费的项目，退费时传退费的项目,2025/05/07去掉notEmpty的注解，与前端约定此字段必传，可以是空集合

    /** 支付详细 */
    @NotEmpty
    private List<PaymentDetailDto> paymentDetails;

    /** 支付批次号 */
    private List<String> chrgBchnoList;//医保预结算时返回，医保结算时必传，否则结算时一直预结算


}
