package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FinancialResourceStatus {
    ACTIVE(1, "ACTIVE", "The status is active."), CANCELLED(2, "CANCELLED", "The status has been cancelled."),
    DRAFT(3, "DRAFT", "The status is in draft mode."),
    IN_ERROR(4, "ENTERED_IN_ERROR", "The status was entered in error.");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static FinancialResourceStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (FinancialResourceStatus val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
