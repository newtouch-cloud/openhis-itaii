package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 处方项目分类代码枚举
 *
 * @author YourName
 * @date 2025-04-21
 */
@Getter
@AllArgsConstructor
public enum YbPrescriptionItemType {

    /**
     * 西药
     */
    WESTERN_MEDICINE("11", "西药"),

    /**
     * 中成药
     */
    TRADITIONAL_CHINESE_MEDICINE_PREPARATION("12", "中成药"),

    /**
     * 中药饮片
     */
    CHINESE_MATERIAL_MEDICATED("13", "中药饮片");

    private String value;
    private String description;

    public static YbPrescriptionItemType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbPrescriptionItemType type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}