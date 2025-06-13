package com.openhis.common.enums;

/**
 * medCategoryCode
 *
 * @author Wuser
 * @date 2025/4/21
 */

import com.github.pagehelper.util.StringUtil;

import com.openhis.common.enums.ybenums.YbDrugMedWay;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药品分类枚举
 *
 * @author YourName
 * @date 2025-04-21
 */
@Getter
@AllArgsConstructor
public enum medCategoryCode {

    TRADITIONAL_CHINESE_MEDICINE("1", "中成药"),

    WESTERN_MEDICINE("2", "西药"),

    EXTERNAL_PURCHASE_MEDICINE("3", "外购药品"),

    CHINESE_HERBAL_MEDICINE("4", "中草药"),

    OTHER("9", "其他");

    private String value;
    private String description;

    public static medCategoryCode getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (medCategoryCode code : values()) {
            if (code.getValue().equals(value)) {
                return code;
            }
        }
        return null;
    }
}