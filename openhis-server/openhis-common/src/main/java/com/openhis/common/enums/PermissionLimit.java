package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限限制
 *
 * @author liuhr
 * @date 2025/3/25
 */
@Getter
@AllArgsConstructor
public enum PermissionLimit implements HisEnumInterface {
    UNRESTRICTED(1, "unrestricted", "非限制使用"),
    RESTRICTED(2, "restricted", "限制使用"),
    SPECIAL(3, "special", "特殊使用");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static PermissionLimit getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PermissionLimit val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}