/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户类型
 *
 * @author zwh
 * @date 2025-03-14
 */
@Getter
@AllArgsConstructor
public enum AccountType implements HisEnumInterface {

    /**
     * 自费
     */
    SELF_PAY(1, "1", "自费"),

    /**
     * 医保
     */
    MEDICAL_INSURANCE(2, "2", "医保");

    private Integer value;
    private String code;
    private String info;

    public static AccountType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (AccountType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
