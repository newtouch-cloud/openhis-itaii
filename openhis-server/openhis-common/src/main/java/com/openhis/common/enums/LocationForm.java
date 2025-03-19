package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LocationForm implements HisEnumInterface {

    SITE(1, "si", "集合点"),
    BUILDING(2, "bu", "建筑"),
    WING(3, "wi", "连廊"),
    WARD(4, "wa", "病区"),
    LEVEL(5, "lvl", "楼层"),
    CORRIDOR(6, "co", "走廊"),
    ROOM(7, "ro", "诊室"),
    BED(8, "bd", "床"),
    VEHICLE(9, "ve", "运输工具"),
    HOUSE(10, "ho", "病房"),
    CABINET(11, "ca", "库房"),
    ROAD(12, "rd", "路"),
    AREA(13, "area", "区域"),
    VIRTUAL(15, "vi", "虚拟"),
    PHARMACY(16, "ph", "药房");
    // PHARMACY_WINDOW(17, "phw", "发药窗口"),
    // PHARMACY_TABLE(18, "dt", "摆药台"),
    // NURSE_STATION(19, "ns", "护士站"),
    // PERSON_STORE(20, "ps", "个人储物柜"),
    // DOCTOR_TABLE(21, "dt", "医生诊台");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
