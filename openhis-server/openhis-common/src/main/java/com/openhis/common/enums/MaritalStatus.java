package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaritalStatus implements HisEnumInterface {

    UNMARRIED(10, "10", "未婚"), MARRIED(20, "20", "已婚"), FIRST_MARRIAGE(21, "21", "初婚"), SECOND_MARRIAGE(22, "22", "再婚"),
    REMARRIED(23, "23", "复婚"), WIDOWED(30, "30", "丧偶"), DIVORCED(40, "40", "离婚"), UNSPECIFIED(90, "90", "未说明的婚姻状况");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static MaritalStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (MaritalStatus val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
