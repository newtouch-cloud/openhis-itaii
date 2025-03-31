/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品调拨
 *
 * @author
 * @date 2025-03-15
 */
@Data
@Accessors(chain = true)
public class InventoryTransferDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String busNo;

    /** 状态 */
    private String statusEnum;

    /** 项目类型 */
    private String itemTable;

    /** 物品编码 */
    private Long itemNo;

    /** 项目名称 */
    private String itemName;

    /** 项目规格 */
    private String itemVolume;

    /** 产品批号 */
    private String lotNumber;

    /** 厂家产地 */
    private String manufacturerId;

    /** 调拨数量 */
    private BigDecimal itemQuantity;

    /** 物品计量单位 */
    private String unitCode;

    /** 采购单价 */
    private BigDecimal price;

    /** 合计金额 总价 */
    private BigDecimal totalPrice;

    /** 源仓库类型 */
    private Integer sourceTypeEnum;

    /** 源仓库 */
    private String sourceName;

    /** 源仓位 */
    private String sourceStoreName;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;

    /** 目的仓库 */
    private String purposeName;

    /** 目的仓位 */
    private String purposeStoreName;

    /** 审批时间 */
    private Date approvalTime;

    /** 有效期至 */
    private Date occurrenceTime;

    /** 申请人 */
    private String applicantName;

    /** 审批人 */
    private String approveName;

    /** 租户ID */
    private Integer tenantId;

}
