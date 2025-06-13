package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否
 */
@Getter
@AllArgsConstructor
public enum Whether implements HisEnumInterface {

    /**
     * 否
     */
    NO(0, "0", "否"),

    /**
     * 是
     */
    YES(1, "1", "是");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static Whether getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (Whether val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
