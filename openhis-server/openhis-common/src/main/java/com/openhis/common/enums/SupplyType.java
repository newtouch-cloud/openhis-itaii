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
    PRODUCT_TRANSFER(2, "2", "商品调拨"),

    /**
     * 汇总发药
     */
    DISPENSING_AGGREGATION(3, "3", "汇总发药"),

    /**
     * 商品盘点
     */
    PRODUCT_STOCKTAKING(4, "4", "商品盘点"),

    /**
     * 采购退货
     */
    PRODUCT_RETURN(5, "5", "采购退货"),

    /**
     * 报损单
     */
    LOSS_REPORT_FORM(6, "6", "报损单"),

    /**
     * 领用出库
     */
    ISSUE_INVENTORY(7, "7", "领用出库"),

    /**
     * 商品批量调拨
     */
    PRODUCT_BATCH_TRANSFER(8, "8", "商品批量调拨"),

    /**
     * 退货出库
     */
    RETURN_ISSUE(9, "9", "退货出库"),

    /**
     * 商品批量盘点
     */
    PRODUCT_BATCH_STOCKTAKING(10, "10", "商品批量盘点");

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
