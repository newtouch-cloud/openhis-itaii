/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 合同类型
 *
 * @author zwh
 * @date 2025-03-29
 */
@Getter
@AllArgsConstructor
public enum ContractCategory implements HisEnumInterface {

    /**
     * 自费
     */
    SELF_CHARGE(1, "1", "自费"),

    /**
     * 公费
     */
    PUBLIC_CHARGE(2, "2", "公费"),

    /**
     * 省医保
     */
    PROVINCIAL_INSURANCE(3, "3", "省医保"),

    /**
     * 市医保
     */
    MUNICIPAL_INSURANCE(4, "4", "市医保"),

    /**
     * 职工医保
     */
    WORKER_INSURANCE(5, "5", "职工医保");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ContractCategory getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ContractCategory val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
