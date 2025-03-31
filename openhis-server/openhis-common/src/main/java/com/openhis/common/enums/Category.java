/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Category 合同类别（结算类别）
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Getter
@AllArgsConstructor
public enum Category {

    /**
     * MEMBER_FIRST
     */
    SELF(0, "自费"),
    /**
     * MEMBER_SECOND
     */
    PROVINCIAL_INSURANCE(1, "MEMBER_SECOND"),
    /**
     * MEMBER_SECOND
     */
    MUNICIPAL_INSURANCE(1, "MEMBER_SECOND"),
    /**
     * MEMBER_SECOND
     */
    PUBLIC(1, "MEMBER_SECOND");
    @EnumValue
    private Integer value;
    private String description;

    public static Category getByValue(Integer value) {
        if (value==null) {
            return null;
        }
        for (Category val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
