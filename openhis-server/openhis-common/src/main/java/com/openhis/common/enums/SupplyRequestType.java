package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SupplyRequestType implements HisEnumInterface {

    DEPARTMENT(2, "2", "科室"), CABINET(11, "ca", "库房"), PHARMACY(16, "ph", "药房");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static SupplyRequestType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (SupplyRequestType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
