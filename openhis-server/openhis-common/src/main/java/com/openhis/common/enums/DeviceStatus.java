package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceStatus {
    ACTIVE (1, "active", "有效"),

    INACTIVE(2, "inactive", "无效"),

    ERROR(3, "entered-in-error", "错误");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
