package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 优先级
 */
@Getter
@AllArgsConstructor
public enum PriorityLevel implements HisEnumInterface {

    EMERGENCY(1, "EM", "紧急"), PRIORITY(2, "PR", "优先"), ORDINARY(3, "OR", "普通"), NOT_URGENT(4, "NU", "不紧急");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static PriorityLevel getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PriorityLevel val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
