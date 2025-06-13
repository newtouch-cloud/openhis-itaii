package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicableScope implements HisEnumInterface {

    HUMAN(1, "human", "人"),

    ANIMAL(2, "animal", "动物"),

    ALL(9, "all", "全部");

    ApplicableScope(int value, String code, String info) {
        this.value = value;
        this.code = code;
        this.info = info;
    }

    @EnumValue
    @JsonValue // 标记响应json值
    private final Integer value;
    private final String code;
    private final String info;

    public static ApplicableScope getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ApplicableScope val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
