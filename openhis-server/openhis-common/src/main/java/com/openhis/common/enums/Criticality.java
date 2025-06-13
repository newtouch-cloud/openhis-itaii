package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 危险程度
 *
 * @author liuhr
 * @date 2025/4/10
 */
@Getter
@AllArgsConstructor
public enum Criticality implements HisEnumInterface {

    LOW(0, "low", "低"),
    HIGH(1, "high", "高"),
    UNASSESSABLE(2, "unassessable", "无法评估");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String code;
    private final String info;
}