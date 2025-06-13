package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContrastTypeEnum implements HisEnumInterface {

    INVOICE_CLINIC(1, "invoice-clinic", "电子发票门诊"),

    INVOICE_CINPATIENT(2, "invoice-inpatient", "电子发票住院");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ContrastTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ContrastTypeEnum val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
