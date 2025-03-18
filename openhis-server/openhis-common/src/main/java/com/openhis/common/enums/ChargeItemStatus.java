package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChargeItemStatus implements HisEnumInterface {

    /**
     * 待收费
     */
    PLANNED(1, "planned", "待收费"),

    /**
     * 待结算
     */
    BILLABLE(2, "billable", "待结算"),

    /**
     * 不可收费
     */
    NOT_BILLABLE(3, "not-billable", "不可收费"),

    /**
     * 终止
     */
    ABORTED(4, "aborted", "终止"),

    /**
     * 已结算
     */
    BILLED(5, "billed ", "已结算"),

    /**
     * 错误
     */
    ERROR(6, "entered-in-error", "错误"),

    /**
     * 未知
     */
    UNKNOWN(7, "unknown", "未知");

    private final Integer value;
    private final String code;
    private final String info;

}
