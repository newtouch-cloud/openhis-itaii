package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author YourName
 * @date 2025-04-22
 */
@Getter
@AllArgsConstructor
public enum YbGender {

    /**
     * 未知的性别
     */
    UNKNOWN("0", "未知的性别"),

    /**
     * 男
     */
    MALE("1", "男"),

    /**
     * 女
     */
    FEMALE("2", "女"),

    /**
     * 未说明性别
     */
    UNSTATED("9", "未说明性别");

    private String value;
    private String description;

    public static YbGender getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbGender type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}