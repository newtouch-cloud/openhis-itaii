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

    UNIT_PRODUCT_BATCH_NUM(1, "1", "产品批号"),
    PROCUREMENT(2,"2","采购"),
    UNIT(3,"3","单位"),
    LIMIT(4,"4","限制");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
