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
 * 合同管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("fin_payment_notice")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PaymentNotice extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 状态 */
    private Integer statusEnum;

    /** 支付通知类型 */
    private Integer typeEnum;

    /** 付款实体ID */
    private Long paypmentReconciliationId;

    /** 被支付方 */
    private Long payee;

    /** 通知方式 */
    private Integer wayCode;

    /** 通知内容 */
    private String content;

    /** 被通知方 */
    private String recipient;

    /** 金额 */
    private BigDecimal amount;

    /** 支付状态 */
    private Integer payStatus;

    /** 机构 */
    private String orgCode;

    /** 删除状态 */
    private Integer deleteFlag;

}