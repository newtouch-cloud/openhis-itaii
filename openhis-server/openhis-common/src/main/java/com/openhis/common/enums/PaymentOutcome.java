/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 付款结果
 *
 * @author SunJQ
 * @date 2025-04-20
 */
@Getter
@AllArgsConstructor
public enum PaymentOutcome {

    /**
     * 排队
     */
    QUEUED("0", 0,"排队"),
    /**
     * 完整
     */
    COMPLETED("1", 1, "完整"),
    /**
     * 错误
     */
    ERROR("2", 3,"错误"),
    /**
     * 部分
     */
    PARTIAL("3", 4, "部分");

    private String value;
    private Integer code;
    private String description;

    public static PaymentOutcome getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (PaymentOutcome val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
