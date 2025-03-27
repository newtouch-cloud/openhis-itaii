package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 拆分属性
 *
 * @author liuhr
 * @date 2025/3/27
 */
@Getter
@AllArgsConstructor
public enum SplitPropertyCode implements HisEnumInterface {

    OUTPATIENT_NON_SPLITTABLE(1, "outpatient_non_splittable", "门诊不可拆分包装单位"),
    INPATIENT_SPLITTABLE(2, "inpatient_splittable", "住院可拆分包装单位"),
    EMERGENCY_SPLITTABLE(3, "emergency_splittable", "急诊可拆分包装单位"),
    PHARMACY_NON_SPLITTABLE(4, "pharmacy_non_splittable", "药房不可拆分包装单位");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}