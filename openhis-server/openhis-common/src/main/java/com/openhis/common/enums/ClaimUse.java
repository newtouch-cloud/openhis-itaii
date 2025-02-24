package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimUse {
    CLAIM(1, "claim", "治疗已完成，这代表服务的索赔。"),
    PREAUTHORIZATION(2, "preauthorization", "治疗已提议，这代表服务的预授权。"),
    PREDETERMINATION(3, "predetermination", "治疗已提议，这代表服务的预决定。");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
