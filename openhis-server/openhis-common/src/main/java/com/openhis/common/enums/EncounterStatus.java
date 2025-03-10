package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EncounterStatus implements HisEnumInterface {
    PLANNED(1, "draft", "待诊"), // 已安排

    IN_PROGRESS(2, "in-progress", "在诊"), // 进行中

    ON_HOLD(3, "on-hold", "暂离"),

    DISCHARGED(4, "on-discharged", "诊毕"), // 诊毕,出院

    COMPLETED(5, "completed", "完成,已结算"),

    CANCELLED(6, "cancelled", "已取消"),

    DISCONTINUED(7, "discontinued", "已中断"),

    IN_ERROR(8, "entered-in-error", "错误"),

    UNKNOWN(9, "unknown", "未知");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
