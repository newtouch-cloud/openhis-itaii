package com.openhis.common.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 特殊病种标志枚举类
 *
 * @author liuhr
 * @date 2025/4/22
 */

@Getter
@AllArgsConstructor
public enum DiagnosisCertainty {


    PRESENT(1, "1", "有"),

    UNDETERMINED(2, "2", "临床未确定"),

    UNKNOWN(3, "3", "情况不明"),

    ABSENT(4, "4", "无");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String description;

    public static DiagnosisCertainty getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (DiagnosisCertainty certainty : values()) {
            if (certainty.getValue().equals(value)) {
                return certainty;
            }
        }
        return null;
    }
}