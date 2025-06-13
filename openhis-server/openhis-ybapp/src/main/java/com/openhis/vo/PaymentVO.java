/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

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
    private Long id;

    /** 状态 */
    private Integer statusEnum;

    /** 关联ID */
    private Long relationId;

    /** 支付的业务标识符 */
    private String paymentNo;

    /** 付款类别 */
    private Integer paymentEnum;

    /** 付款实体ID */
    private Long paymentReconciliationId;

    /** 发起支付的工作流程类别 */
    private Integer kindEnum;

    /** 收款员 */
    private Long entererId;

    /** 支付发起人类型 */
    private Integer issuerEnum;

    /** 支付的患者ID */
    private Long patientId;

    /** 请求支付责任人ID */
    private Long practitionerId;

    /** 付款结果 */
    private Integer outcomeEnum;

    /** 支付位置 */
    private Long locationId;

    /** 到期时间 */
    private Date expirationDate;

    /** 应收金额 */
    private BigDecimal tenderedAmount;

    /** 找零金额 */
    private BigDecimal returnedAmount;

    /** 付款总额 */
    private BigDecimal displayAmount;

    /** 打印标识 */
    private Integer printCount;

    /** 合同编码 */
    private String contractNo;

    /** 处方号集合 */
    private String chargeItemIds;

    /** 就诊ID */
    private Long encounterId;

    /** 结算时间 */
    private Date billDate;

    /** 发票编号 */
    private Long invoiceId;

    /** 关联账户Id */
    private Long accountId;

    /** 医保清算标志 */
    private Integer ybClearFlag;
}
