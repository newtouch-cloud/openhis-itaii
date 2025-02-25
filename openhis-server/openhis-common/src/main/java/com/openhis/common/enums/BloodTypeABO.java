package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 血型ABO
 *
 * @author liuhr
 * @date 2025/2/25
 */
@Getter
@AllArgsConstructor
public enum BloodTypeABO {

    TYPE_A(1, "typeA", "A 型"),
    TYPE_B(2, "typeB", "B 型"),
    TYPE_O(3, "typeO", "O 型"),
    TYPE_AB(4, "typeAB", "AB 型"),
    OTHER(5, "other", "不详"),
    UNKNOWN(9, "unknown", "未查");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
