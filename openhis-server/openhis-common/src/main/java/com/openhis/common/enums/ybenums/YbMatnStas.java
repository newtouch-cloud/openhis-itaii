/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 生育状态
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Getter
@AllArgsConstructor
public enum YbMatnStas {

    // 未知
    UNKNOWN("0", "未知"),

    // 非妊娠期或哺乳期
    NON_PREGNANT_OR_LACTATING("1", "非妊娠期或哺乳期"),

    // 近期有生育计划
    PLANNING_TO_CONCEIVE("2", "近期有生育计划"),

    // 妊娠期
    PREGNANT("3", "妊娠期"),

    // 哺乳期
    LACTATING("4", "哺乳期");

    private String value;
    private String description;

    public static YbMatnStas getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbMatnStas val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
