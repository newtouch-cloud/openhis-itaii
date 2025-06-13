/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 进销存类型（inv_data_type）
 *
 * @author SunJQ
 * @date 2025-04-19
 */
@Getter
@AllArgsConstructor
public enum YbInvDataType {
    // 1. 盘存信息
    STOCK_TAKING_INFO(1,"1", "盘存信息"),

    // 2. 库存变更信息
    INVENTORY_CHANGE_INFO(2,"2", "库存变更信息"),

    // 3. 采购信息
    PURCHASE_INFO(3,"3", "采购信息"),

    // 4. 销售信息
    SALES_INFO(4,"4", "销售信息");

    private final int code;
    private final String value;
    private final String name;

    public static YbInvDataType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbInvDataType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
