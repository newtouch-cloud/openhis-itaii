/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 未执行原因
 *
 * @author zwh
 * @date 2025-03-29
 */
@Getter
@AllArgsConstructor
public enum NotPerformedReason {

    /**
     * 退费
     */
    REFUND(1, "1", "退费"),

    /**
     * 异常
     */
    ERROR(9, "9", "异常");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
