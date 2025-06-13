package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 命中条件枚举
 */
@Getter
@AllArgsConstructor
public enum ConditionCode implements HisEnumInterface {

    /**
     * 批号
     */
    LOT_NUMBER(1, "1", "产品批号"),

    /**
     * 采购
     */
    PURCHASE(2, "2", "采购"),

    /**
     * 单位
     */
    UNIT(3, "3", "单位"),

    /**
     * 限制
     */
    LIMIT(4, "4", "限制");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ConditionCode getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ConditionCode val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
