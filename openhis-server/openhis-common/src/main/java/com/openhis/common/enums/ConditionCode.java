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

    UNIT_PRODUCT_BATCH_NUM(1, "unit_product_batch_num", "单位,产品批号");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
