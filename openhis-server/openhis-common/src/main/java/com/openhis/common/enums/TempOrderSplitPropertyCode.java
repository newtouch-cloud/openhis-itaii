package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 住院临时医嘱拆分属性的枚举
 *
 * @author liuhr
 *
 * @date 2025/3/28
 */
@Getter
@AllArgsConstructor
public enum TempOrderSplitPropertyCode implements HisEnumInterface {

    TEMP_ORDER_MIN_UNIT_CEIL(1, "temp_order_min_unit_ceil", "临时医嘱按最小单位每次量向上取整"), // 可以拆分
    TEMP_ORDER_PACKAGE(2, "temp_order_package", "临时医嘱按包装单位开立"), // 不可拆分
    TEMP_ORDER_MIN_UNIT_TOTAL_CEIL(3, "temp_order_min_unit_total_ceil", "临时医嘱按最小单位总量向上取整"), // 可以拆分
    TEMP_ORDER_PACKAGE_UNIT_CEIL(4, "temp_order_package_unit_ceil", "临时医嘱按包装单位每次量向上取整");// 不可拆分

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static TempOrderSplitPropertyCode getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (TempOrderSplitPropertyCode val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}