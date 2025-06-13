package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionClinicalStatus {
    ACTIVE(1, "ACTIVE", "活跃The disease is currently active."),
    RECURRENCE(2, "RECURRENCE", "复发The disease has recurred after a period of remission."),
    RELAPSE(3, "RELAPSE", "再发The disease has relapsed after a period of improvement."),
    INACTIVE(4, "INACTIVE", "不活跃The disease is currently inactive."),
    REMISSION(5, "REMISSION", "体验复发风险The disease is in remission."),
    RESOLVED(6, "RESOLVED", "康复The disease has been resolved."),
    UNKNOWN(7, "UNKNOWN", "位置The disease status is unknown.");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ConditionClinicalStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ConditionClinicalStatus val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
