package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {

    ACTIVE (1, "active", "有效"),

    INACTIVE(2, "inactive", "无效"),

    ERROR(3, "entered-in-error", "错误"),

    ON_HOLD(4, "on-hold", "冻结"),

    UNKNOWN(5, "unknown", "未知");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
