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
    ADMITTER(1, "1", "接诊医生"),

    ATTENDER(2, "2", "责任护士"),

    CONTACT(3, "3", "联系人"),

    CONSULTANT(4, "4", "顾问"),

    DISCHARGER(5, "5", "出院办理人"),

    ESCORT(6, "6", "护送人"),

    REFERRER(7, "7", "推荐人"),

    EMERGENCY(8, "8", "紧急联系人"),

    ATTENDING_DOCTOR(9, "9", "住院医生"),

    CHIEF_DOCTOR(10, "10", "主任医生"),

    PRINCIPAL_DOCTOR(11, "11", "主治医生"),

    REGISTRATION_DOCTOR(12, "12", "挂号医生");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ParticipantType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ParticipantType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
