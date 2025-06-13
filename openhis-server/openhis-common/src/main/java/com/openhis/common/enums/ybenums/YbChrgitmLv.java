/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.openhis.common.enums.HisEnumInterface;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医保等级【chrgitm_lv】
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Getter
@AllArgsConstructor
public enum YbChrgitmLv implements HisEnumInterface {

    // 甲类
    CATEGORY_A(1, "1", "甲类"),

    // 乙类
    CATEGORY_B(2, "2", "乙类"),

    // 自费
    SELF_PAY(3, "3", "自费");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static YbChrgitmLv getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (YbChrgitmLv val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }

}
