package com.openhis.administration.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.AccountBillingStatus;
import com.openhis.common.enums.AccountStatus;
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

    // 2025.04.10 经确认此处原有的【1 CASH 个人现金账户 | 2 医保账户】改为【01医保电子凭证 | 02 居民身份证 | 03 社会保障卡 | 04 个人现金账户】
    /** 账户类型编码 */
    private String typeCode;

    /** 编码 */
    private String no;

    /** 名称 */
    private String name;

    /** 患者id */
    private Long patientId;

    /** 就诊id */
    private Long encounterId;

    /** 账户余额 */
    private BigDecimal balanceAmount;

    /** 医保区域编码 */
    private String ybAreaNo;

    /** 合同编码 */
    private String contractNo;

    /** 欠费限制额度 */
    private BigDecimal limitAccount;

    /** 是否为就诊登记使用 */
    private Integer encounterFlag;

}
