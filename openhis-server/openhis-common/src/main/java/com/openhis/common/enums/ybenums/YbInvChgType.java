/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 7.51 库存变更类型(inv_chg_type)
 *
 * @author SunJQ
 * @date 2025-04-17
 */
@Getter
@AllArgsConstructor
public enum YbInvChgType {

    // 调拨入库
    ALLOCATION_IN("101", "调拨入库"),
    // 调拨出库
    ALLOCATION_OUT("102", "调拨出库"),
    // 盘盈
    INVENTORY_GAIN("103", "盘盈"),
    // 盘损
    INVENTORY_LOSS("104", "盘损"),
    // 销毁
    DESTRUCTION("105", "销毁"),
    // 其他入库
    OTHER_IN("106", "其他入库"),
    // 其他出库
    OTHER_OUT("107", "其他出库"),
    // 商品采购入库
    PURCHASE_IN("108", "商品采购入库"),
    // 商品退货出库
    RETURN_OUT("109", "商品退货出库"),
    // 赠药入库
    DONATION_IN("110", "赠药入库"),
    // 赠药退回出库
    DONATION_RETURN_OUT("111", "赠药退回出库");

    private String value;
    private String description;

    public static YbInvChgType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbInvChgType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
