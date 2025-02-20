package com.openhis.administration.domain;

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
 * 就诊账户管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_account")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Account extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 状态枚举 */
    private Integer statusEnum;

    /** 结账状态枚举 */
    private Integer billingStatusEnum;

    /** 账户类型编码 */
    private Integer typeCode;

    /** 名称 */
    private String name;

    /** 患者id */
    private Long patientId;

    /** 就诊id */
    private Long encounterId;

    /** 账户余额 */
    private BigDecimal balanceAmount;

    /** 医保区域编码 */
    private String ybAreaCode;

    /** 合同编码 */
    private String contractCode;

    /** 欠费限制额度 */
    private BigDecimal limitAccount;

    /** 删除状态 */
    private Integer deleteFlag;

}