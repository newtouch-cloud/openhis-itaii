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
@TableName("fin_payment_rec_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PaymentRecDetail extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** recId */
    private Long predecessor;

    /** 付款类型 */
    private Integer targetEnum;

    /** 账户 */
    private Long accountId;

    /** 账户类型 */
    private Integer accountType;

    /** 支付类型 */
    private Integer payType;

    /** 支付类型等级 */
    private Integer payTypeClass;

    /** 金额 */
    private BigDecimal amount;

    /** 找零 */
    private BigDecimal returnAmount;

    /** 交款 */
    private BigDecimal chargeAmount;

    /** 支付平台返回交易号 */
    private String payTransId;

    /** 支付平台返回交易信息 */
    private String payTransText;

    /** 支付平台返回交易时间 */
    private Date payTransDate;

    /** 支付平台账前余额 */
    private BigDecimal beforeBalance;

    /** 支付平台账后余额 */
    private BigDecimal afterBalance;

    /** 单笔交易结果 */
    private Integer resultEnum;


}