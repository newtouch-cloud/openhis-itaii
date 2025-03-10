/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收费项目类型
 *
 * @author zwh
 * @date 2025-03-10
 */
@Getter
@AllArgsConstructor
public enum ChargeItemContext {

    /**
     * 采购
     */
    PURCHASE(1, "采购"),

    /**
     * 处方
     */
    PRESCRIPTION(2, "处方");

    private Integer value;
    private String info;

    public static ChargeItemContext getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ChargeItemContext val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
