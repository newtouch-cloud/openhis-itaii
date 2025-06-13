package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.AccountBillingStatus;
import com.openhis.common.enums.AccountStatus;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊账号 表单数据
 */
@Data
@Accessors(chain = true)
public class AccountFormData {

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 患者id */
    @NotNull(message = "患者id不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 状态枚举 */
    private Integer statusEnum;

    /** 结账状态枚举 */
    private Integer billingStatusEnum;

    /** 账户类型编码 */
    private String typeCode; // 【01医保电子凭证 | 02 居民身份证 | 03 社会保障卡 | 04 个人现金账户】

    /** 名称 */
    private String name; // 刷医保卡时应该会带出该信息

    /** 账户余额 */
    private BigDecimal balanceAmount; // 刷医保卡时应该会带出该信息

    /** 医保区域编码 */
    private String ybAreaNo; // 刷医保卡时应该会带出该信息

    /** 合同编码 */
    private String contractNo; // 从选择的费用性质里选择合同编码

    /** 欠费限制额度 */
    private BigDecimal limitAccount; // 刷医保卡时应该会带出该信息

    /**
     * 设置默认值
     */
    public AccountFormData() {
        this.contractNo = CommonConstants.BusinessName.DEFAULT_CONTRACT_NO;
        this.statusEnum = AccountStatus.ACTIVE.getValue();
        this.billingStatusEnum = AccountBillingStatus.OPEN.getValue();
    }

}
