/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出入院诊断类别(inout_diag_type)  出入诊断类别
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Getter
@AllArgsConstructor
public enum YbInOutDiagType {
    /**
     * 入院诊断
     */
    YB_IN_DIAG_TYPE("1", "入院诊断"),
    /**
     * 出院诊断
     */
    YB_OUT_DIAG_TYPE("2", "出院诊断");

    private String value;
    private String description;

    public static YbInOutDiagType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbInOutDiagType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
