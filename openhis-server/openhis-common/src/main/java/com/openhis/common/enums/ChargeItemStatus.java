package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChargeItemStatus {
    PLANNED (1, "planned", "待收费"),

    BILLABLE(2, "billable", "待结算"),

    NOT_BILLABLE(3, "not-billable", "不可收费"),

    ABORTED(4, "aborted", "终止"),

    BILLED (5, "billed ", "已结算"),

    ERROR(6, "entered-in-error", "错误"),

    UNKNOWN(7, "unknown", "未知");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
