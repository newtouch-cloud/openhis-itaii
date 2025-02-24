package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LocationMode {
    INSTANCE (1, "instance", "具体"),

    KIND(2, "Kind", "种类");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
