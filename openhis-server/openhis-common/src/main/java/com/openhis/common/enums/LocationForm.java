package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LocationForm {

    SITE(1, "si", "集合点A collection of buildings or other locations such as a site or a campus."),
    BUILDING(2, "bu", "建筑Any Building or structure. This may contain rooms, corridors, wings, etc. It might not have walls, or a roof, but is considered a defined/allocated space."),
    WING(3, "wi", "连廊A Wing within a Building, this often contains levels, rooms and corridors."),
    WARD(4, "wa", "病区A Ward is a section of a medical facility that may contain rooms and other types of location."),
    LEVEL(5, "lvl", "楼层A Level in a multi-level Building/Structure."),
    CORRIDOR(6, "co", "走廊Any corridor within a Building, that may connect rooms."),
    ROOM(7, "ro", "诊室A space that is allocated as a room, it may have walls/roof etc., but does not require these."),
    BED(8, "bd", "床A space that is allocated for sleeping/laying on. This is not the physical bed/trolley that may be moved about, but the space it may occupy."),
    VEHICLE(9, "ve", "运输工具A means of transportation."),
    HOUSE(10, "ho", "病房A residential dwelling. Usually used to reference a location that a person/patient may reside."),
    CABINET(11, "ca", "库房A container that can store goods, equipment, medications or other items."),
    ROAD(12, "rd", "路A defined path to travel between 2 points that has a known name."),
    AREA(13, "area", "区域A defined physical boundary of something, such as a flood risk zone, region, postcode"),
    JURISDICTION(14, "jdn", "适用范围A wide scope that covers a conceptual domain, such as a Nation (Country wide community or Federal Government - e.g. Ministry of Health), Province or State (community or Government), Business (throughout the enterprise), Nation with a business scope of an agency (e.g. CDC, FDA etc.) or a Business segment (UK Pharmacy), not just an physical boundary"),
    VIRTUAL(15, "vi", "虚拟A location that is virtual in nature, such as a conference call or virtual meeting space");

//    PHARMACY(16, "ph", "药房"),
//    PHARMACY_WINDOW(17, "phw", "发药窗口"),
//    PHARMACY_TABLE(18, "dt", "摆药台"),
//    NURSE_STATION(19, "ns", "护士站"),
//    PERSON_STORE(20, "ps", "个人储物柜"),
//    DOCTOR_TABLE(21, "dt", "医生诊台");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
