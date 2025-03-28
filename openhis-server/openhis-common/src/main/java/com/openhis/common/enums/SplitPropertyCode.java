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

    OUTPATIENT_MIN_UNIT_CEIL(1, "outpatient_min_unit_ceil", "门诊按最小单位每次量向上取整"),
    OUTPATIENT_PACKAGE_NON_SPLITTABLE(2, "outpatient_package_non_splittable", "门诊按包装单位不可拆分"),
    OUTPATIENT_MIN_UNIT_TOTAL_CEIL(3, "outpatient_min_unit_total_ceil", "门诊按最小单位总量向上取整"),
    OUTPATIENT_PACKAGE_UNIT_CEIL(4, "outpatient_package_unit_ceil", "门诊按包装单位每次量向上取整");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}