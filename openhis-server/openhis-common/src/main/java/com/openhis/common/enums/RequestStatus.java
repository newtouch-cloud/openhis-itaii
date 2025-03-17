/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请求状态
 */
@Getter
@AllArgsConstructor
public enum RequestStatus implements HisEnumInterface {

    /**
     * 待发送
     */
    DRAFT(1, "draft", "待发送"),

    /**
     * 已发送
     */
    COMPLETED(2, "completed", "已发送"),

    /**
     * 暂停
     */
    ON_HOLD(3, "on_hold", "暂停"),

    /**
     * 撤回
     */
    CANCELLED(4, "cancelled", "撤回"),

    /**
     * 停止
     */
    STOPPED(5, "stopped", "停止"),

    /**
     * 不可用
     */
    ENDED(6, "ended", "不可用"),

    /**
     * 未知
     */
    UNKNOWN(7, "unknown", "未知"),

    /**
     * 已汇总
     */
    SUMMARIZED(8, "summarized", "已汇总");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
