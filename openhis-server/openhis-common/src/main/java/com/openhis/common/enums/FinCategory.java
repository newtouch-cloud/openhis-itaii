package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FinCategory implements HisEnumInterface {

    /**
     * 自费
     */
    SELF_PAY(1, "1", "自费"),

    /**
     * 省医保
     */
    PROVINCIAL_MEDICAL_INSURANCE(2, "2", "省医保"),

    /**
     * 市医保
     */
    MUNICIPAL_MEDICAL_INSURANCE(3, "3", "市医保");

    private Integer value;
    private String code;
    private String info;

    public static FinCategory getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (FinCategory val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }

    public static FinCategory getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (FinCategory val : values()) {
            if (val.getCode().equals(code)) {
                return val;
            }
        }
        return null;
    }
}
