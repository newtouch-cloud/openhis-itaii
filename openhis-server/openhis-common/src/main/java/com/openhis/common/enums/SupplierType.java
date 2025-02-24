package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SupplierType {
    DRAFT(1, "1", "生产商"),

    ACTIVE(2, "2", "供应商");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
