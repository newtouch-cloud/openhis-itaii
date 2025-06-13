package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别 0=男,1=女,2=未知(和若依框架保持一致)
 */
@Getter
@AllArgsConstructor
public enum AdministrativeGender implements HisEnumInterface {

    MALE(0, "male", "男性"),

    FEMALE(1, "female", "女性"),

    UNKNOWN(2, "unknown", "未知");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static AdministrativeGender getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (AdministrativeGender val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
