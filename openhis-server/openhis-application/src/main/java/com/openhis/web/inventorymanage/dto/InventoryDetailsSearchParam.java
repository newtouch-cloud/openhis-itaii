/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存相关明细查询条件
 *
 * @author
 * @date 2025-03-10
 */
@Data
@Accessors(chain = true)
public class InventoryDetailsSearchParam implements Serializable {

    /** -------------------------共通------------------------- */
    /** 单据号 */
    private String busNo;

    /** 项目名称 */
    private String itemName;

    /** 项目编码 */
    private String itemId;

    /** 仓库 */
    private String locationId;

    /** 制单日期 */
    private Date occurrenceTime;

    /** -------------------------采购入库用------------------------- */
    /** 项目类型 */
    private Integer categoryType;

    /** -------------------------商品盘点用------------------------- */
    /** 盘点状态 */
    private String stockStatus;
}
