/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

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
     * 付款id
     */
    private Long id;//paymentId

    /** 就诊ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 收费项 */
    private List<Long> chargeItemIds;//收费时传收费的项目，退费时传退费的项目,2025/05/07去掉notEmpty的注解，与前端约定此字段必传，可以是空集合

    /** 支付详细 */
    @NotEmpty
    private List<PaymentDetailDto> paymentDetails;

    private String ybMdtrtCertType;//社保卡/身份证/电子医保码

    private String busiCardInfo;//社保卡号/身份证号/ecToken
}
