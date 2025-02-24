package com.openhis.common.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum EncounterClass {
    IMP(1, "IMP", "住院"),

    AMB(2, "AMB", "门诊"),

    OBSENC(3, "OBSENC", "留观"),

    EMER(4, "EMER", "急诊"),

    VR(5, "VR", "线上"),

    HH(6, "HH", "家庭");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
