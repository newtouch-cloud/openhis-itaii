/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 清算类别
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Getter
@AllArgsConstructor
public enum YbClrType {

    /**
     * 门诊
     */
    OUTPATIENT_CLINIC("11", "门诊"),
    /**
     * 药店购药
     */
    PHARMACY_PURCHASE("41", "药店购药"),
    /**
     * 住院
     */
    INPATIENT_CARE("21", "住院"),
    /**
     * 其他
     */
    OTHER("99", "其他");

    private String value;
    private String description;

    public static YbClrType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbClrType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
