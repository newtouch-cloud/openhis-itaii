/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

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

    /** 供应商 */
    private Long supplierId;

    /** 经手人 */
    private Long practitionerId;
}
