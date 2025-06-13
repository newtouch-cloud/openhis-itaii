/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 清算状态
 *
 * @author SunJQ
 * @date 2025-04-17
 */
@Getter
@AllArgsConstructor
public enum YbClrStas {

    /**
     * 已申请
     */
    CLR_STAS10("10", "已申请"),
    /**
     * 已受理
     */
    CLR_STAS20("20", "已受理"),
    /**
     * 已清算
     */
    CLR_STAS50("50", "已清算");

    private String value;
    private String description;

    public static YbClrStas getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbClrStas val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
