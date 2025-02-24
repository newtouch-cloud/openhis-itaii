package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionVerificationStatus {
    UNCONFIRMED(1, "UNCONFIRMED", "未确诊The disease status is unconfirmed."),
    PROVISIONAL(2, "PROVISIONAL", "疑似诊断The disease status is provisional."),
    DIFFERENTIAL(3, "DIFFERENTIAL", "多个诊断选项之一The diagnosis is one of several possibilities."),
    CONFIRMED(4, "CONFIRMED", "已确诊The disease status has been confirmed."),
    REFUTED(5, "REFUTED", "已排除The disease status has been refuted."),
    ENTERED_IN_ERROR(6, "ENTERED_IN_ERROR", "The disease status was entered in error.");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
