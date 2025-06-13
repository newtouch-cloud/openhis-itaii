package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LocationStatus implements HisEnumInterface {

    ACTIVE(1, "active", "启用"),

    INACTIVE(2, "inactive", "停用"),

    SUSPENDED(3, "suspended", "临时停用");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static LocationStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (LocationStatus val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
