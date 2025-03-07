package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EncounterLocationStatus implements HisEnumInterface {
    PLANNED(1, "planned", "已安排"),

    ACTIVE(2, "active", "使用中"),

    RESERVED(3, "reserved", "已保留"),

    COMPLETED(4, "completed", "已用完");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
