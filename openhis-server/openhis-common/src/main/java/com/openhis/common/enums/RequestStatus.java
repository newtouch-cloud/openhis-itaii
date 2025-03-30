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
    ACTIVE(2, "active", "已发送"),

    /**
     * 已完成
     */
    COMPLETED(3, "completed", "已完成"),

    /**
     * 暂停
     */
    ON_HOLD(4, "on_hold", "暂停"),

    /**
     * 取消
     */
    CANCELLED(5, "cancelled", "取消"),

    /**
     * 停止
     */
    STOPPED(6, "stopped", "停止"),

    /**
     * 不可用
     */
    ENDED(7, "ended", "不可用"),

    /**
     * 已汇总
     */
    SUMMARIZED(8, "summarized", "已汇总"),

    /**
     * 未知
     */
    UNKNOWN(9, "unknown", "未知");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
