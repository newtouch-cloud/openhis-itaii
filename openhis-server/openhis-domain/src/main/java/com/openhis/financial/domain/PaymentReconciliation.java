package com.openhis.financial.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 付款管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("fin_payment_reconciliation")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PaymentReconciliation extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
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
    private Long paypmentReconciliationId;

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
    private String chargeItemJson;

    /** 就诊ID */
    private Long encounterId;


}