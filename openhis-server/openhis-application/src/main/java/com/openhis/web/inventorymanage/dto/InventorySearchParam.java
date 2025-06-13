/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 入库单据分页查询条件
 *
 * @author zwh
 * @date 2025-02-18
 */
@Data
@Accessors(chain = true)
public class InventorySearchParam {

    /** 状态 */
    private Integer statusEnum;

    /** 单据类型 */
    private Integer typeEnum;

    /** 供应商 */
    private Long supplierId;

    /** 经手人 */
    private Long practitionerId;

    /**
     * 单据时间
     */
    private Date createTime;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;

    /** 仓库 */
    private String locationId;

    /** 货位 */
    private String locationStoreId;
}
