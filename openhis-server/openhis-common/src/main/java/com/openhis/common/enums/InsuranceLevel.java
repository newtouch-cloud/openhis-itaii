package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医保等级枚举类
 *
 * @author liuhr
 * @date 2025/4/16
 */
@Getter
@AllArgsConstructor
public enum InsuranceLevel implements HisEnumInterface {

    CLASS_A(1, "1", "甲类"), CLASS_B(2, "2", "乙类"), SELF_PAY(3, "3", "自费");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String code;
    private final String info;

}
