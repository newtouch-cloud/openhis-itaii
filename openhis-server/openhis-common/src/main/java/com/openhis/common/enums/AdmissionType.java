package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入院类型
 *
 * @author liuhr
 * @date 2025/4/7
 */
@Getter
@AllArgsConstructor
public enum AdmissionType implements HisEnumInterface {

    ACCIDENT(1, "accident", "事故"), FREE_CHOICE(2, "free_choice", "随意选择的"), URGENT(3, "urgent", "紧急"),
    DELIVERY(4, "delivery", "分娩"), NEWBORN(5, "newborn", "新生儿"), NORMAL(6, "normal", "常规"),
    CRITICAL(7, "critical", "危重");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static AdmissionType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (AdmissionType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}