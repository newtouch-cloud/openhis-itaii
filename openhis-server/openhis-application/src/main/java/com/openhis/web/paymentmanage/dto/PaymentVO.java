/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 映射对象
 *
 * @author SunJQ
 * @date 2025-05-06
 */
@Data
public class PaymentVO {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;//付款id

    /** 状态 */
    @Dict(dictCode = "payment_status")
    private Integer statusEnum;
    private String statusEnum_dictText;//状态

    /** 支付的业务标识符 */
    private String paymentNo;//业务流水

    /** 付费/退款 */
    @Dict(dictCode = "payment_enum")
    private String paymentEnum;//付费/退款
    private String paymentEnum_dictText;

    /** 收款员 */
    private Long entererId;//收款员

    /** 收款员 */
    private String entererName;//收款员

    /** 支付的患者ID */
    private Long patientId;//支付患者

    /** 支付的患者 */
    private String patientName;//支付患者

    /** 付款结果 */
    @Dict(dictCode = "outcome_enum")
    private Integer outcomeEnum;//付款结果
    private String outcomeEnum_dictText;

    /** 应收金额 */
    private BigDecimal tenderedAmount;//应收

    /** 找零金额 */
    private BigDecimal returnedAmount;//找零

    /** 付款总额 */
    private BigDecimal displayAmount;//付款总额

    /** 打印标识 */
    private Integer printCount;//打印标志

    /** 结算时间 */
    private Date billDate;//结算时间

    /** 发票编号 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long invoiceId;//发票id

    /** 发票no */
    private String invoiceNo;//发票no

    /** 发票no */
    private String encounterBusNo;//发票no

    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;//就诊id
    private String encounterIdStr;//就诊id

    @JsonSerialize(using = ToStringSerializer.class)
    private String paymentId;//付款id

    private String ybSettleIds;//医保付款id
}
