/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存相关明细
 *
 * @author
 * @date 2025-03-10
 */
@Data
@Accessors(chain = true)
public class InventoryDetailsPageDto implements Serializable {

    /** ------------------------------------共通------------------------------------ */
    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String busNo;

    /** 单据状态 */
    private String statusEnum;

    /** 项目类型 */
    private Integer itemTable;
    private String itemTableText;

    /** 项目编码 */
    private Long itemNo;

    /** 项目名称 */
    private String itemName;

    /** 项目规格 */
    private String itemVolume;

    /** 厂家 */
    private String manufacturerText;

    /** 产品批号 */
    private String lotNumber;

    /** 数量 */
    private BigDecimal itemQuantity;

    /** 单价 */
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 物品计量单位 */
    private String unitCode;

    /** 目的仓库类型 */
    private Integer purposeType;
    private String purposeTypeText;

    /** 目的仓库 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_location")
    private Long purposeLocationId;
    private String purposeLocationId_dictText;

    /** 目的仓位 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_location")
    private Long purposeLocationStoreId;
    private String purposeLocationStoreId_dictText;

    /** 部门 */
    private String orgName;

    /** 审批时间 */
    private Date approvalTime;

    /** 有效时间 */
    private Date expirationTime;

    /** 申请人（制单人） */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long applicantId;
    private String applicantIddictText;

    /** 审批人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long approverId;
    private String approverId_dictText;

    /** ------------------------------------采购入库用------------------------------------ */
    /** 供应商 */
    private String supName;

    /** ------------------------------------商品调拨用------------------------------------ */
    /** 源仓库类型 */
    private Integer sourceType;
    private String sourceTypeText;

    /** 源仓库 */
    private String sourceLocation;

    /** 源货位 */
    private String sourceLocationStore;

    /** ------------------------------------商品盘点用------------------------------------ */
    /** 盘前数量 */
    private BigDecimal beforeStockQuantity;

    /** 盘后数量 */
    private BigDecimal afterStockQuantity;

    /** 盈亏数量 */
    private BigDecimal lossQuantity;

    /** 盈亏原因 */
    private String reason;

    /** ------------------------------------领用出库用------------------------------------ */
    /** 采购批次流水号 */
    private String supplyBusNo;

    /** 期望时间（制单日期） */
    private String occurrenceTime;
}
