/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * YbDrordBhvr (0: MEMBER_FIRST; 1: MEMBER_SECOND;)
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Getter
@AllArgsConstructor
public enum YbDrordBhvr {

    /**
     * MEMBER_FIRST
     */
    OTHER("0", "其他"),
    /**
     * 出院带药
     */
    DISCHARGE_MEDICATION("1", "出院带药");

    private String value;
    private String description;

    public static YbDrordBhvr getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbDrordBhvr val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
