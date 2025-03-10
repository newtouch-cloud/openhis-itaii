/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目类型
 *
 * @author zwh
 * @date 2025-03-10
 */
@Getter
@AllArgsConstructor
public enum ItemCategory {

    /**
     * 中药
     */
    CHINESE_MEDICINE(1, "中药"),

    /**
     * 西药
     */
    WESTERN_MEDICINE(2, "西药"),

    /**
     * 中成药
     */
    CHINESE_PATENT_MEDICINE(3, "中成药"),

    /**
     * 医疗耗材
     */
    MEDICAL_CONSUMABLES(4, "医疗耗材");

    private Integer value;
    private String info;

    public static ItemCategory getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ItemCategory val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
