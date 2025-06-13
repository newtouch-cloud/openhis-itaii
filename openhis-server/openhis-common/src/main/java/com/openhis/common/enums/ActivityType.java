package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 活动类型
 */
@Getter
@AllArgsConstructor
public enum ActivityType implements HisEnumInterface {

    PROOF(1, "PROOF", "检验"),

    TEST(2, "TEST", "检查"),

    CARE(3, "CARE", "护理"),

    OPERATION(4, "OPERATION", "手术"),

    OTHER(5, "OTHER", "其他");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ActivityType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ActivityType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
