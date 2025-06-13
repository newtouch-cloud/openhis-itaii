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
     * 省医保
     */
    PROVINCIAL_INSURANCE(1, "省医保"),
    /**
     * 市医保
     */
    MUNICIPAL_INSURANCE(2, "市医保"),
    /**
     * 公费
     */
    PUBLIC(3, "公费");
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
