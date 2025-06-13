package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审方药师职称枚举
 *
 * @author liuhr
 * @date 2025-04-22
 */
@Getter
@AllArgsConstructor
public enum YbPharmacistTitle {

    /**
     * 执业药师
     */
    LICENSED_PHARMACIST("1", "执业药师"),

    /**
     * 卫生技术职称
     */
    HEALTH_TECH_TITLE("2", "卫生技术职称"),

    /**
     * 主任药师
     */
    CHIEF_PHARMACIST("2.1", "主任药师"),

    /**
     * 副主任药师
     */
    DEPUTY_CHIEF_PHARMACIST("2.2", "副主任药师"),

    /**
     * 主管药师
     */
    SENIOR_PHARMACIST("2.3", "主管药师"),

    /**
     * 药师
     */
    PHARMACIST("2.4", "药师"),

    /**
     * 药士
     */
    JUNIOR_PHARMACIST("2.5", "药士");

    private String value;
    private String description;

    public static YbPharmacistTitle getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbPharmacistTitle title : values()) {
            if (title.getValue().equals(value)) {
                return title;
            }
        }
        return null;
    }
}