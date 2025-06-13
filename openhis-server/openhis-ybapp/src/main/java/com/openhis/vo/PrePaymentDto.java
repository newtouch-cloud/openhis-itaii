/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 预结算入参
 *
 * @author SunJQ
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
public class PrePaymentDto {

    /** 收款员 */
    private Long entererId;

    /** 支付的患者ID */
    @NotNull
    private Long patientId;

    /** 就诊ID */
    private Long encounterId;

    /** 收费项 */
    @NotEmpty
    private List<Long> chargeItemIds;//

    private String busiCardInfo;//社保卡号/身份证号/ecToken
}
