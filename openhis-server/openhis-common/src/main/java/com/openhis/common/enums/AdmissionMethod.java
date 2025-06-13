package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入院方式
 *
 * @author liuhr
 * @date 2025/4/7
 */
@Getter
@AllArgsConstructor
public enum AdmissionMethod implements HisEnumInterface {

    OUTPATIENT(1, "outpatient", "门诊入院"), EMERGENCY(2, "emergency", "急诊入院"), TRANSFER_IN(3, "transfer_in", "转入入院"),
    TRANSFER_FROM_OTHER(4, "transfer_from_other", "其它医疗机构转入"), OTHER(5, "other", "其它");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static AdmissionMethod getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (AdmissionMethod val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}