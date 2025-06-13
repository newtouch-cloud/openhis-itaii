/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊患者账单 dto
 *
 * @author zwh
 * @date 2025-03-17
 */
@Data
@Accessors(chain = true)
public class EncounterPatientPaymentDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 就诊ID */
    private Long encounterId;

    /** 处方号 */
    private String prescriptionNo;

    /** 关联账户ID */
    private Long accountId;

    /** 支付状态 */
    private Integer statusEnum;

    /** 关联ID */
    private Long relationId;

    /** 支付的业务标识符 */
    private String paymentNo;

    /** 付款类别 */
    private Integer paymentEnum;

    /** 支付位置 */
    @Dict(dictTable = "adm_location", dictText = "name", dictCode = "id")
    private Long locationId;
    private String locationId_dictText;

    /** 到期时间 */
    private Date expirationDate;

    /** 应收金额 */
    private BigDecimal tenderedAmount;

    /** 找零金额 */
    private BigDecimal returnedAmount;

    /** 付款总额 */
    private BigDecimal displayAmount;

    /** 合同编码 */
    private String contractNo;

}
