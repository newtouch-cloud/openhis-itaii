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

    DEFINITION(2, "definition", "科室");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
