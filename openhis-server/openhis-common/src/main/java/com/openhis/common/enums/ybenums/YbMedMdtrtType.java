/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * [3101]就诊类型
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Getter
@AllArgsConstructor
public enum YbMedMdtrtType {

    /**
     * 门诊
     */
    OUTPATIENT_CLINIC("1", "门诊"),
    /**
     * 药店购药
     */
    PHARMACY_PURCHASE("2", "药店购药"),
    /**
     * 住院
     */
    INPATIENT_CARE("3", "住院"),
    /**
     * 其他
     */
    OTHER("4", "其他");

    private String value;
    private String description;

    public static YbMedMdtrtType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbMedMdtrtType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
