package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 治疗时长类型
 */
@Getter
@AllArgsConstructor
public enum TherapyTimeType implements HisEnumInterface {

    LONG_TERM(1, "LT", "长期"),

    TEMPORARY(2, "TEMP", "临时"),

    SEASONAL(3, "SS", "季节性");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

}
