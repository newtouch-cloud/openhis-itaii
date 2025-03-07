package com.openhis.common.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum EncounterType implements HisEnumInterface {

    INITIAL(1, "initial", "初诊"),
    FOLLOW_UP(2, "follow-up", "复诊");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
