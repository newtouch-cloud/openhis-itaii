/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PaymentType (0: MEMBER_FIRST; 1: MEMBER_SECOND;)
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Getter
@AllArgsConstructor
public enum PaymentType {

    /**
     * 付费
     */
    PAY(0, "付费"),
    /**
     * 退费
     */
    UN_PAY(1, "退费");
    @EnumValue
    private Integer value;
    private String description;

    public static PaymentType getByValue(String value) {
        if(value==null){
            return null;
        }
        for (PaymentType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
