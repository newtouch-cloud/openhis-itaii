/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 7.43 医嘱类别drord_type
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Getter
@AllArgsConstructor
public enum YbDrordType {

    // 临时医嘱
    TEMPORARY_ORDER(1, "临时医嘱"),

    // 长期医嘱
    STANDING_ORDER(2, "长期医嘱"),

    // 备用医嘱
    STANDBY_ORDER(3, "备用医嘱");

    private Integer value;
    private String description;

    public static YbDrordType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbDrordType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
