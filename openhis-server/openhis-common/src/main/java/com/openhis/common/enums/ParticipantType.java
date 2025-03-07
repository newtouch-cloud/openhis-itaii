package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参与者类型
 */
@Getter
@AllArgsConstructor
public enum ParticipantType implements HisEnumInterface {
    ADMITTER(1, "1", "首诊医生"),

    ATTENDER(2, "2", "责任护士"),

    CONTACT(3, "3", "联系人"),

    CONSULTANT(4, "4", "顾问"),

    DISCHARGER(5, "5", "出院办理人"),

    ESCORT(6, "6", "护送人"),

    REFERRER(7, "7", "推荐人"),

    EMERGENCY(8, "8", "紧急联系人");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
