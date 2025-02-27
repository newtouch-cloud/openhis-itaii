package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityDefCategory {
    MEDICAL_SERVICE_ITEM(1, "medical_service_item", "医疗服务项"),

    TREATMENT_SURGERY(2, "treatment_surgery", "手术与治疗");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
