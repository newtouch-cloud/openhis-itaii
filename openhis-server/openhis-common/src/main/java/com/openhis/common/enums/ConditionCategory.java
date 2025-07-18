package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionCategory {
    PROBLEM(1, "problem-list-item", "问题列表"),
    DIAGNOSIS(2, "encounter-diagnosis", "就诊诊断");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
