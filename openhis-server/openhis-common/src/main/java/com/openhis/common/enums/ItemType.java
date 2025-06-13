/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

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
public enum ItemType implements HisEnumInterface {

    /**
     * 药品
     */
    MEDICINE(1, "1", "药品"),

    /**
     * 耗材
     */
    DEVICE(2, "2", "耗材"),

    /**
     * 医疗活动
     */
    ACTIVITY(3, "3", "医疗活动");

    @EnumValue
    private Integer value;
    private String code;
    private String info;

    public static ItemType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ItemType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
