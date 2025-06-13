package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 身份标识的状态枚举
 *
 * @author liuhr
 * @date 2025/5/14
 */
@Getter
@AllArgsConstructor
public enum IdentifierStatusEnum implements HisEnumInterface {

    USUAL(1, "usual", "常规"),
    OFFICIAL(2, "official", "官方"),
    TEMP(3, "temp", "临时"),
    SECONDARY(4, "secondary", "次要"),
    OLD(5, "old", "旧");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static IdentifierStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (IdentifierStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}