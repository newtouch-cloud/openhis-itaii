/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 单据分页列表 dto
 *
 * @author zwh
 * @date 2025-02-18
 */
@Data
@Accessors(chain = true)
public class ReceiptPageDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String supplyBusNo;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 状态 */
    private Integer typeEnum;
    private String typeEnum_enumText;

    /** 供应商 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_supplier")
    private Long supplierId;
    private String supplierId_dictText;

    /** 经手人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long practitionerId;
    private String practitionerId_dictText;

    /** 审批人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long approverId;
    private String approverId_dictText;

    /** 审批时间 */
    private Date approvalTime;

    /** 申请人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long applicantId;
    private String applicantId_dictText;

    /** 申请时间 */
    private Date applyTime;

    /** 制单日期 */
    private Date occurrenceTime;

}
