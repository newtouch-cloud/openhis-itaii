package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 活动类型
 */
@Getter
@AllArgsConstructor
public enum StatisticsType implements HisEnumInterface {

    OUTPATIENT_REVENUE(1, "1", "门诊整体收入明细"),
    OUTPATIENT_YB_REVENUE(2, "2", "门诊医保收入明细"),
    OUTPATIENT_SELF_REVENUE(3, "3", "门诊自费收入明细"),
    //OUTPATIENT_STUDENT_REVENUE(4, "4", "门诊学生费用收入明细"),
    OUTPATIENT_REFUND(5, "5", "门诊退费明细");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static StatisticsType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (StatisticsType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
