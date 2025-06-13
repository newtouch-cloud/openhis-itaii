/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药品类型
 *
 * @author zwh
 * @date 2025-03-10
 */
@Getter
@AllArgsConstructor
public enum CategoryType implements HisEnumInterface {

    // 西药中成药
    WESTERN_AND_CHINESE_PATENT_MEDICINE(101, "101","西药中成药"),
    // 重要饮片
    IMPORTANT_HERBAL_SLICES(102, "102","重要饮片"),
    // 自制剂
    SELF_PREPARED_MEDICATION(103, "103","自制剂"),
    // 民族药
    ETHNIC_MEDICINE(104, "104","民族药"),
    // 医疗服务项目
    MEDICAL_SERVICE_ITEM(201,"201", "医疗服务项目"),
    // 医用耗材
    MEDICAL_CONSUMABLES(301,"301", "医用耗材");

    @EnumValue
    private Integer value;
    private String code;
    private String info;

    public static CategoryType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (CategoryType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }

    public static CategoryType getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (CategoryType val : values()) {
            if (val.getCode().equals(code)) {
                return val;
            }
        }
        return null;
    }

}
