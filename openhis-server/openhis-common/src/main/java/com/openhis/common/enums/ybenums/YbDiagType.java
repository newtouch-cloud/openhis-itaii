/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 诊断类别
 *
 * @author SunJQ
 * @date 2025-04-14
 */
@Getter
@AllArgsConstructor
public enum YbDiagType {

    /**
     * 西医诊断
     */
    WESTERN_MEDICINE_DIAGNOSIS(1, "西医诊断"),
    /**
     * 中医主病诊断
     */
    TCM_MAIN_DISEASE_DIAGNOSIS(2, "中医主病诊断"),
    /**
     * 中医主证诊断
     */
    TCM_MAIN_SYNDROME_DIAGNOSIS(3, "中医主证诊断");

    private Integer value;
    private String description;

    public static YbDiagType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbDiagType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
