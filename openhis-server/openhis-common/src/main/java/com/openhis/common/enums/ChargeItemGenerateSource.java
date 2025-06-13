/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账单生成来源
 *
 * @author zwh
 * @date 2025-03-10
 */
@Getter
@AllArgsConstructor
public enum ChargeItemGenerateSource implements HisEnumInterface {

    /**
     * 医生开立
     */
    DOCTOR_PRESCRIPTION(1, "1", "医生开立"),

    /**
     * 护士划价
     */
    NURSE_PRICING(2, "2", "护士划价"),

    /**
     * 医嘱绑定
     */
    MEDICAL_ORDER_BINDING(3, "3", "医嘱绑定");

    private final Integer value;
    private final String code;
    private final String info;

    public static ChargeItemGenerateSource getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ChargeItemGenerateSource val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
