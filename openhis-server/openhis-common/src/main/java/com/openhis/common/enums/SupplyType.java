/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 单据类型
 *
 * @author zwh
 * @date 2025-03-05
 */
@Getter
@AllArgsConstructor
public enum SupplyType implements HisEnumInterface {

    /**
     * 采购入库
     */
    PURCHASE_INVENTORY(1, "1", "采购入库"),

    /**
     * 商品调拨
     */
    PRODUCT_ALLOCATION(2, "2", "商品调拨"),

    /**
     * 汇总发药
     */
    DISPENSING_AGGREGATION(3, "3", "汇总发药"),

    /**
     * 商品盘点
     */
    PRODUCT_INVENTORY(4, "4", "商品盘点");

    private Integer value;
    private String code;
    private String info;

    public static SupplyType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (SupplyType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
