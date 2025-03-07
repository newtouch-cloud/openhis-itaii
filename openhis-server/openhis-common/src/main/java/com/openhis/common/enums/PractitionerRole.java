package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参与者角色
 */
@Getter
@AllArgsConstructor
public enum PractitionerRole implements HisEnumInterface {
    DOCTOR(1, "doctor", "医生"),

    NURSE(2, "nurse", "护士"),

    PHARMACIST(3, "pharmacist", "药师");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
