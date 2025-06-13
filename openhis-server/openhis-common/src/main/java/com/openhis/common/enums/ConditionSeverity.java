package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionSeverity {
    SEVERE(1, "severe", "严重的"), MIDGRADE(2, "midgrade", "中等的"), MILD(3, "mild", "温和的");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ConditionSeverity getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ConditionSeverity val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
