package com.openhis.common.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum PublicationStatus {

    DRAFT(1, "draft", "草稿"),

    ACTIVE(2, "active", "有效"),

    RETIRED(3, "retired", "停用"),

    UNKNOWN(4, "unknown", "未知");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
