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
 * 采购入库
 *
 * @author
 * @date 2025-03-10
 */
@Data
@Accessors(chain = true)
public class PurchaseInDetailDto implements Serializable {

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

    /** 供应商 */
    private String supName;

    /** 数量 */
    private BigDecimal itemQuantity;

    /** 单价 */
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 物品计量单位 */
    private String unitCode;

    /** 目的仓库 */
    private String locName;

    /** 目的仓位 */
    private String locstoreName;

    /** 审批时间 */
    private Date approvalTime;

    /** 期望时间 */
    private Date occurrenceTime;

    /** 申请人 */
    private String applicantName;

    /** 审批人 */
    private String approverName;

    /** 租户ID */
    private Integer tenantId;

}
