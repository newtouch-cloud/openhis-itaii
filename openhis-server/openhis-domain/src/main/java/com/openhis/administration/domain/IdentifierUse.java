package com.openhis.administration.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdentifierUse {
    USUAL(1, "USUAL", "Usual item"),
    OFFICIAL(2, "OFFICIAL", "Official item"),
    TEMP(3, "TEMP", "Temporary item"),
    SECONDARY(4, "SECONDARY", "Secondary item"),
    OLD(5, "OLD", "Old item");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
