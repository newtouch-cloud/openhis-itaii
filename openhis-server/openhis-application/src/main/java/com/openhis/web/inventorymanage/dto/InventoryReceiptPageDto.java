/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 入库单据分页列表 dto
 *
 * @author zwh
 * @date 2025-02-18
 */
@Data
@Accessors(chain = true)
public class InventoryReceiptPageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String supplyBusNo;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 供应商 */
    private Long supplierId;

    /** 经手人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long practitionerId;

    /** 审批人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long approverId;

    /** 审批时间 */
    private Date approvalTime;

    /** 申请人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long applicantId;

    /** 申请时间 */
    private Date applyTime;

}
