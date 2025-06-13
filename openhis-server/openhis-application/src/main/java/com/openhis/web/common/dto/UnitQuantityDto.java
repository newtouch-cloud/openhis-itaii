/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.common.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 单位数量列表
 *
 * @author zwh
 * @date 2025-04-01
 */
@Data
@Accessors(chain = true)
public class UnitQuantityDto {

    /** 当前库存数量(最小单位) */
    private BigDecimal minQuantity;

    /** 当前库存数量(包装单位) */
    private BigDecimal maxQuantity;
}
