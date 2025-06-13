/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * YbClrWay (0: MEMBER_FIRST; 1: MEMBER_SECOND;)
 *
 * @author SunJQ
 * @date 2025-04-29
 */
@Getter
@AllArgsConstructor
public enum YbClrWay {

    /**
     * 按项目
     */
    ITEM_BY_PROJECT("01", "按项目"),
    /**
     * 按人头
     */
    PER_CAPITA("04", "按人头"),
    /**
     * 单病种
     */
    SINGLE_DISEASE("02", "单病种"),
    /**
     * DRGs点数法
     */
    DRGS_POINTS("05", "DRGs点数法"),
    /**
     * 按床日
     */
    BY_BED_DAY("03", "按床日"),
    /**
     * 其他
     */
    OTHER("99", "其他");

    private String value;
    private String description;

    public static YbClrWay getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbClrWay val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
