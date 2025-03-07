/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 单据类别
 *
 * @author zwh
 * @date 2025-03-05
 */
@Getter
@AllArgsConstructor
public enum SupplyCategory {

    /**
     * 库存供应
     */
    STOCK_SUPPLY(1, "库存供应"),

    /**
     * 非库存供应
     */
    NON_STOCK(2, "非库存供应");

    private Integer value;
    private String info;

    public static SupplyCategory getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (SupplyCategory val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
