/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PaymentResult (0: MEMBER_FIRST; 1: MEMBER_SECOND;)
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Getter
@AllArgsConstructor
public enum PaymentResult {

    /**
     * 未支付
     */
    UNPAID(0, "未支付"),
    /**
     * 已支付
     */
    PAID(1, "已支付"),
    /**
     * 已退费
     */
    REFUNDED(-2, "已退费");

    private Integer value;
    private String description;

    public static PaymentResult getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PaymentResult val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
