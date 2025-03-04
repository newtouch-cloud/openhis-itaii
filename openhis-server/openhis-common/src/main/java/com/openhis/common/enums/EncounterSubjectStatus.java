package com.openhis.common.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum EncounterSubjectStatus implements HisEnumInterface {
    PLANNED(1, "arrived", "已到达"),

    TRIAGED(2, "triaged", "已分诊"),

    RECEIVING_CARE(3, "receiving-care", "已看诊"),

    ON_LEAVE(4, "on-leave", "已离开"),

    DEPARTED(5, "departed", "已完成");


    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
