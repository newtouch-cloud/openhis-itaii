package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.github.pagehelper.util.StringUtil;
import com.openhis.common.enums.ybenums.YbRxItemTypeCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 处方类别
 *
 * @author liuhr
 * @date 2025/4/21
 */
@Getter
@AllArgsConstructor
public enum PrescriptionType implements HisEnumInterface {

    OUTPATIENT_WESTERN_OR_TRADITIONAL_CHINESE_MEDICINE(1, "1", "门诊西药中成药"),

    OUTPATIENT_CHINESE_MATERIAL_MEDICATED(2, "2", "门诊中药饮片"),

    EMERGENCY_WESTERN_OR_TRADITIONAL_CHINESE_MEDICINE(3, "3", "急诊西药中成药"),

    EMERGENCY_CHINESE_MATERIAL_MEDICATED(4, "4", "急诊中药饮片"),

    PEDIATRIC_WESTERN_OR_TRADITIONAL_CHINESE_MEDICINE(5, "5", "儿科西药中成药"),

    PEDIATRIC_CHINESE_MATERIAL_MEDICATED(6, "6", "儿科中药饮片"),

    NARCOTIC_OR_SPIRIT_FIRST_CLASS(7, "7", "麻、精一"),

    SPIRIT_SECOND_CLASS(8, "8", "精二"),

    CHINESE_MATERIAL_MEDICATED(9, "9", "中药饮片"),

    TRADITIONAL_CHINESE_MEDICINE_PREPARATION(10, "10", "中成药"),

    INPATIENT_WESTERN_OR_TRADITIONAL_CHINESE_MEDICINE(11, "11", "住院西药中成药"),

    OTHER(99, "99", "其它");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static PrescriptionType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PrescriptionType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }

}
