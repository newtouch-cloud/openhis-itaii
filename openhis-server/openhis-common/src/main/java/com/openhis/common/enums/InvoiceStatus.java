package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoiceStatus {
    DRAFT(1, "draft", "发票已经准备好了，但尚未定稿。"),

    ISSUED(2, "issued", "发票已定稿并发给了收款人。"),

    BALANCED(2, "balanced", "发票已结清/完全付清了。"),

    CANCELLED(2, "cancelled", "这张发票作废了。"),

    ERROR(3, "entered-in-error", "这张发票在开出去之前就被确定为是填错了的。");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
