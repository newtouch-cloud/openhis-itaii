package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 严重程度
 *
 * @author liuhr
 * @date 2025/4/10
 */
@Getter
@AllArgsConstructor
public enum Severity implements HisEnumInterface {

    MILD(0, "mild", "轻微"),
    MODERATE(1, "moderate", "中度"),
    SEVERE(2, "severe", "严重");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String code;
    private final String info;
}