package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 组套类型
 *
 * @author yangmo
 * @date 2025-04-11
 */

@Getter
@AllArgsConstructor
public enum OrderGroupType implements HisEnumInterface {

    ORDER(1, "1", "医嘱组套"),

    CHINESE(2, "2", "中医协定处方(组套)"),

    CHARGEITEM(3, "3", "划价组套");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static OrderGroupType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (OrderGroupType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
