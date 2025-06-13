/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存信息
 *
 * @author yangmo
 * @date 2025-04-08
 */
@Data
@Accessors(chain = true)
public class InventoryDto implements Serializable {

    /** 库存ID */
    private Long inventoryId;

    /** 发药ID */
    private Long dispenseId;

    /** 拆零单位 */
    private String inventoryUnitCode;

    /** 发放单位 */
    private String dispenseUnit;

    /** 当前库存数量(拆零单位) */
    private BigDecimal inventoryQuantity;

    /** 已发数量 */
    private BigDecimal dispenseQuantity;

    /** 发药数量 */
    private BigDecimal quantity;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 药品名称 */
    private String itemName;

    /** 库存状态 */
    private Integer inventoryStatusEnum;
}
