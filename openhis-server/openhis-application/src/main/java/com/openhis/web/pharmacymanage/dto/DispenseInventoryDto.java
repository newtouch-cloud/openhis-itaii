/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 药品发放和库存表连接信息
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class DispenseInventoryDto {

    /** 药品发放表主键ID */
    private Long dispenseId;

    /** 请求单位编码 */
    private String dispenseUnitCode;

    /** 请求数量 */
    private Integer dispenseQuantity;

    /** 库存项目表主键ID */
    private Long inventoryId;

    /** 常规单位 */
    private String inventoryBaseUnitCode;

    /** 当前库存数量(常规单位) */
    private BigDecimal inventoryBaseQuantity;

    /** 最小单位 */
    private String inventoryMinUnitCode;

    /** 当前库存数量(最小单位数量) */
    private BigDecimal inventoryMinQuantity;

    /** 拆零比 */
    private BigDecimal partPercent;
}
