/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否标识
 *
 * @author zwh
 * @date 2025-03-05
 */
@Getter
@AllArgsConstructor
public enum Whether {

    /**
     * 否
     */
    NO(0, "否"),

    /**
     * 是
     */
    YES(1, "是");

    private Integer value;
    private String info;

    public static Whether getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (Whether val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
