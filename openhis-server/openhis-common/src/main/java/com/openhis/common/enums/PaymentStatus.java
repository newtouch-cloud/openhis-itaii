/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态
 *
 * @author zwh
 * @date 2025-03-17
 */
@Getter
@AllArgsConstructor
public enum PaymentStatus {

    /**
     * 草稿
     */
    DRAFT(0, "0", "草稿"),

    /**
     * 支付成功
     */
    SUCCESS(1, "1", "支付成功"),

    /**
     * 支付取消
     */
    CANCEL(2, "2", "支付取消"),

    /**
     * 全部退款
     */
    REFUND_ALL(3, "3", "全部退款"),

    /**
     * 部分退款
     */
    REFUND_PART(4, "4", "部分退款"),

    /**
     * 错误
     */
    ERROR(9, "9", "错误");

    private Integer value;
    private String code;
    private String info;

    public static PaymentStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PaymentStatus val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
