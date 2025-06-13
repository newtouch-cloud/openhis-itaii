/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import com.openhis.web.paymentmanage.dto.PaymentDetailDto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 患者退款信息列表
 *
 * @author zwh
 * @date 2025-05-05
 */
@Data
@Accessors(chain = true)
public class EncounterPatientRefundDto {

    /** 支付ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long paymentId;

    /** 收款人ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long entererId;

    /** 就诊ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 支付的患者ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 处方号 */
    private String prescriptionNo;

    /** 收费项目编号 */
    private String busNo;

    /** 合同编号 */
    private String contractNo;

    /** 项目名 */
    private String itemName;

    /** 项目名 */
    private String orgName;

    /** 收费项目ids */
    private String chargeItemIds;

    /** 收款人 */
    private String entererName;

    /** 收费状态 */
    private Integer chargeStatus;
    private String chargeStatus_enumText;

    /** 支付状态 */
    private Integer paymentStatus;
    private String paymentStatus_enumText;

    /** 发放状态 */
    private Integer dispenseStatus;
    private String dispenseStatus_enumText;

    /** 执行状态 */
    private Integer serviceStatus;
    private String serviceStatus_enumText;

    /** 数量 */
    private Integer quantityValue;

    /** 已发药数量 */
    private Integer dispenseQuantity;

    /** 单位 */
    @Dict(dictCode = "unit_code")
    private String quantityUnit;
    private String quantityUnit_dictText;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 结算时间 */
    private Date billDate;

    /**
     * 支付明细列表
     */
    private List<PaymentDetailDto> paymentDetailList;
}
