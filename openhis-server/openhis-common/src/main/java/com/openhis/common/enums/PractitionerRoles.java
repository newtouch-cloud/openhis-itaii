package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参与者角色 : 对应 sys_role表的角色标识 ; 需要补充业务数据时在该枚举类添加
 */
@Getter
@AllArgsConstructor
public enum PractitionerRoles implements HisEnumInterface {

    DOCTOR(1, "doctor", "医生"),

    NURSE(2, "nurse", "护士"),

    PHARMACIST(3, "pharmacist", "药师"),

    LOCATION_ADMIN(4, "locationAdmin", "库房管理员");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
