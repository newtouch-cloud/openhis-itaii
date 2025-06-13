/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 7.48 目录类别(list_type)
 *
 * @author SunJQ
 * @date 2025-04-17
 */
@Getter
@AllArgsConstructor
public enum YbListType {
    // 西药中成药
    WESTERN_AND_CHINESE_PATENT_MEDICINE(101, "101","西药中成药"),
    // 中药饮片
    IMPORTANT_HERBAL_SLICES(102, "102","中药饮片"),
    // 自制剂
    SELF_PREPARED_MEDICATION(103, "103","自制剂"),
    // 民族药
    ETHNIC_MEDICINE(104, "104","民族药"),
    // 医疗服务项目
    MEDICAL_SERVICE_ITEM(201,"201", "医疗服务项目"),
    // 医用耗材
    MEDICAL_CONSUMABLES(301,"301", "医用耗材");

    private final int code;
    private final String value;
    private final String name;

    public static YbListType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbListType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
