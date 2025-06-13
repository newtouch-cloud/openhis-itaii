/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 退费入参
 *
 * @author SunJQ
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
public class CancelPaymentDto {
    /**
     * id
     */
    private Long id;

    /** 收费项 */
    private List<Long> chargeItemIds;//收费时传收费的项目，退费时传退费的项目,2025/05/07去掉notEmpty的注解，与前端约定此字段必传，可以是空集合

    /** 支付详细 */
    @NotEmpty
    private List<PaymentDetailDto> paymentDetails;

    /** 支付批次号 */
    private String chrgBchno;//医保预结算时返回，医保结算时必传，否则结算时一直预结算（弃用）

    private String ybMdtrtCertType;//社保卡/身份证/电子医保码

    private String busiCardInfo;//社保卡号/身份证号/ecToken

    private String reason;//退款理由

    private String setlId;//结算id
}
