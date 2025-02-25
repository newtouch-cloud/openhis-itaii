package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 血型RH
 *
 * @author liuhr
 * @date 2025/2/25
 */
@Getter
@AllArgsConstructor
public enum BloodTypeRH {

    TYPE_RH_NEGATIVEB(1, "typeRHNegative", "Rh 阴性"),
    TYPE_RH_POSITIVE(2, "typeRHPositive", "Rh 阳性"),
    OTHER(3, "other", "不详"),
    UNKNOWN(4, "unknown", "未查");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
