package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityDefCategory implements HisEnumInterface {
    MEDICAL_SERVICE_ITEM(1, "medical_service_item", "医疗服务项"),

    TREATMENT_SURGERY(2, "treatment_surgery", "手术与治疗");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ActivityDefCategory getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ActivityDefCategory val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
