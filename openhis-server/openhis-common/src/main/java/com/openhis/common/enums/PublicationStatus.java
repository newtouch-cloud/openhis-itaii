package com.openhis.common.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum PublicationStatus {

    DRAFT(1, "draft", "草稿"),

    ACTIVE(2, "active", "有效"),

    RETIRED(3, "retired", "停用"),

    UNKNOWN(4, "unknown", "未知");

    PublicationStatus(int value, String code, String info) {
        this.value = value;
        this.code = code;
        this.info = info;
    }

    @EnumValue
    @JsonValue // 标记响应json值
    private final Integer value;
    private final String code;
    private final String info;
}
