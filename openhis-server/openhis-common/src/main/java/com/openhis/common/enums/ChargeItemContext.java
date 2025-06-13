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
public enum ChargeItemContext implements HisEnumInterface {

    /**
     * 药品
     */
    MEDICATION(1, "1", "药品"),

    /**
     * 耗材
     */
    DEVICE(2, "2", "耗材"),

    /**
     * 项目
     */
    ACTIVITY(3, "3", "项目"),

    /**
     * 挂号
     */
    REGISTER(4, "4", "挂号");

    private final Integer value;
    private final String code;
    private final String info;

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
