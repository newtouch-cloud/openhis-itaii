/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医嘱操作类型
 */
@Getter
@AllArgsConstructor
public enum AdviceOpType implements HisEnumInterface {

    /**
     * 保存医嘱
     */
    SAVE_ADVICE(1, "1", "保存医嘱"),

    /**
     * 签发医嘱
     */
    SIGN_ADVICE(2, "2", "签发医嘱");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static AdviceOpType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (AdviceOpType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
