package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 诊断绑定类型
 */
@Getter
@AllArgsConstructor
public enum BindingType implements HisEnumInterface {

    PERSONAL(1, "personal", "个人"),

    DEFINITION(2, "definition", "科室"),

    HOSPITAL(3, "hospital", "全院");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static BindingType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (BindingType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
