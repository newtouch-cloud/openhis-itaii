package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 就诊类型
 */
@Getter
@AllArgsConstructor
public enum EncounterClass implements HisEnumInterface {
    IMP(1, "IMP", "住院"),

    AMB(2, "AMB", "门诊"),

    OBSENC(3, "OBSENC", "留观"),

    EMER(4, "EMER", "急诊"),

    VR(5, "VR", "线上"),

    HH(6, "HH", "家庭"),

    OTHER(7, "OTHER", "其他");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static EncounterClass getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (EncounterClass val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
