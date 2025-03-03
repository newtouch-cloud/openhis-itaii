package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdministrativeGender implements ValueEnum {
    MALE(1, "male", "男性"),
    FEMALE(2, "female", "女性"),
    OTHER(3, "other", "其他"),
    UNKNOWN(9, "unknown", "未知");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
