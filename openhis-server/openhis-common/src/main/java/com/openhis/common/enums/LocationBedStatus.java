package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LocationBedStatus implements HisEnumInterface {
    C(1, "C", "关闭"),
    H(2, "H", "整理"),
    O(3, "O", "占用"),
    U(4, "U", "空闲"),
    K(5, "K", "污染"),
    I(6, "I", "隔离");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
