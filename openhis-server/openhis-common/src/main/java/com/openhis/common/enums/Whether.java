package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否
 */
@Getter
@AllArgsConstructor
public enum Whether implements HisEnumInterface {

    /**
     * 否
     */
    NO(0, "no", "否"),

    /**
     * 是
     */
    YES(1, "yes", "是");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
