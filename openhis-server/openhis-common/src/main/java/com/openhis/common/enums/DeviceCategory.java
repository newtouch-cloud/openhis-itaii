package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceCategory implements HisEnumInterface  {
    ACTIVE (1, "1", "有源的"),
    COMMUNICATING(2, "2", "通讯类"),
    HOMEUSE(3, "3", "非院内使用"),
    IMPLANTABLE(4, "4", "植入类"),
    IN_VITRO(5, "5", "试管类"),
    POINT_OF_CARE(6, "6", "床旁类"),
    SINGLE_USE(7, "7", "单次消耗类"),
    REUSABLE(8, "8", "可重用的"),
    SOFTWARE(9, "9", "软件类"),
    DME(10, "10", "治疗设备");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
