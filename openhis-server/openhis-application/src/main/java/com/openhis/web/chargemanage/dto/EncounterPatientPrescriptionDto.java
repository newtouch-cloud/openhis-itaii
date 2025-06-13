/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊患者处方 dto
 *
 * @author zwh
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class EncounterPatientPrescriptionDto {

    /** 收费项目类型 */
    private Integer contextEnum;
    private String contextEnum_enumText;

    /** 收费状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 就诊ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 患者id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 开立科室 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requestingOrgId;

    /** 数量 */
    private Long quantityValue;

    /** 单位 */
    private String quantityUnit;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 处方号 */
    private String prescriptionNo;

    /** 业务编码 */
    private String busNo;

    /** 收款人ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictCode = "id", dictTable = "adm_practitioner", dictText = "name")
    private Long entererId;
    private String entererId_dictText;

    /** 开立时间 */
    private Date enteredDate;

    /** 收费时间 */
    private Date billDate;

    /** 关联账户ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /** 物品编码 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    /** 物品名称 */
    private String itemName;

    /** 特病标识 */
    @Dict(dictCode = "med_type")
    private String medTypeCode;
    private String medTypeCode_dictText;

    /** 合同编码 */
    private String contractNo;

    /** 医保编码 */
    private String ybNo;

    /** 合同名称 */
    private String contractName;
}
