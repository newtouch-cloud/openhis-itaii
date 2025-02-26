package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否
 */
@Getter
@AllArgsConstructor
public enum WhetherContainUnknown {
    NO(0, "no", "否"),
    YES(1, "yes", "是"),
    UNKNOWN(2, "unknown", "未知");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
