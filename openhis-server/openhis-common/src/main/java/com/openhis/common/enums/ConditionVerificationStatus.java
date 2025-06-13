package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证状态
 */
@Getter
@AllArgsConstructor
public enum ConditionVerificationStatus implements HisEnumInterface {
    UNCONFIRMED(1, "UNCONFIRMED", "未确诊"),

    PROVISIONAL(2, "PROVISIONAL", "疑似"),

    DIFFERENTIAL(3, "DIFFERENTIAL", "有差异"),

    CONFIRMED(4, "CONFIRMED", "已确诊"),

    REFUTED(5, "REFUTED", "已排除"),

    ENTERED_IN_ERROR(6, "ENTERED_IN_ERROR", "未知");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ConditionVerificationStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ConditionVerificationStatus val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
