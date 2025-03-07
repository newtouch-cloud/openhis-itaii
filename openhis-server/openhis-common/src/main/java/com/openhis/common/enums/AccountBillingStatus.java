package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountBillingStatus implements HisEnumInterface {

    OPEN (1, "open", "可用"),

    CARECOMPLETE_NOTBILLED(2, "carecomplete-notbilled", "已出院未结账"),

    CLOSED_BADDEBT(3, "closed-baddebt", "坏账"),

    CLOSED_VOIDED(4, "closed-voided", "不再记账"),

    CLOSED_COMPLETED(5, "closed-completed", "已结账"),

    CLOSED_COMBINED(6, "closed-combined", "已合并");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
