package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 过敏与不耐受中临床状况
 *
 * @author liuhr
 * @date 2025/3/6
 */
@Getter
@AllArgsConstructor
public enum ClinicalStatus implements HisEnumInterface {
    ACTIVE(1, "active", "阳性"), INACTIVE(2, "inactive", "阴性"), RESOLVED(3, "resolved", "已解决"),
    UNKNOWN(4, "unknown", "未知");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ClinicalStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ClinicalStatus val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
