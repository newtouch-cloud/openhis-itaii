package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimType {
    INSTITUTIONAL(1, "Institutional", "医院、诊所和通常的住院索赔。"), ORAL(2, "Oral", "牙科、假牙和口腔卫生索赔。"),
    PHARMACY(3, "Pharmacy", "药品和服务的药店索赔。"),
    PROFESSIONAL(4, "Professional", "通常是来自医生、心理学家、脊椎治疗师、物理治疗师、言语病理学家、康复专家、咨询师的门诊索赔。"),
    VISION(5, "Vision", "视力索赔，包括专业服务和产品，如眼镜和隐形眼镜。");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ClaimType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ClaimType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
