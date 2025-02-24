package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceCategory {
    ACTIVE (1, "active", "有源的"),
    COMMUNICATING(2, "communicating", "通讯类"),
    HOMEUSE(3, "communicating", "非院内使用"),
    IMPLANTABLE(4, "implantable", "植入类"),
    IN_VITRO(5, "in-vitro", "试管类"),
    POINT_OF_CARE(6, "point-of-care", "床旁类"),
    SINGLE_USE(7, "single-use", "单次消耗类"),
    REUSABLE(8, "reusable", "可重用的"),
    SOFTWARE(9, "software", "软件类"),
    DME(10, "dme", "治疗设备");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
