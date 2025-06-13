package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 开单医生职称枚举
 *
 * @author YourName
 * @date 2025-04-22
 */
@Getter
@AllArgsConstructor
public enum YbDoctorTitle {

    /**
     * 主任医师
     */
    CHIEF_PHYSICIAN("231", "主任医师"),

    /**
     * 副主任医师
     */
    DEPUTY_CHIEF_PHYSICIAN("232", "副主任医师"),

    /**
     * 主治医师
     */
    ATTENDING_PHYSICIAN("233", "主治医师"),

    /**
     * 医师
     */
    PHYSICIAN("234", "医师"),

    /**
     * 医士
     */
    MEDICAL_ASSISTANT("235", "医士");

    private String code;
    private String description;

    public static YbDoctorTitle getByValue(String code) {
        if (StringUtil.isEmpty(code)) {
            return null;
        }
        for (YbDoctorTitle title : values()) {
            if (title.getCode().equals(code)) {
                return title;
            }
        }
        return null;
    }
}