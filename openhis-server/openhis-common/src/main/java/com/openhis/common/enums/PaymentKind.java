/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PaymentKind (0: MEMBER_FIRST; 1: MEMBER_SECOND;)
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Getter
@AllArgsConstructor
public enum PaymentKind {

    /**
     * 住院存款
     */
    HOSPITAL_DEPOSIT(0, "住院存款"),
    /**
     * 门诊收费
     */
    OUTPATIENT_CLINIC(1, "门诊收费"),
    /**
     * 住院费用
     */
    INPATIENT_CLINIC(2, "住院费用");

    @EnumValue
    private Integer value;
    private String description;

    public static PaymentKind getByValue(String value) {
        if(value==null){
            return null;
        }
        for (PaymentKind val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
