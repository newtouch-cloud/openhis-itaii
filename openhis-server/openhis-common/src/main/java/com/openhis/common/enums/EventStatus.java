package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件状态
 *
 * @author lyx
 * @date 2025-03-05
 */
@Getter
@AllArgsConstructor
public enum EventStatus implements HisEnumInterface {

    /**
     * 草稿
     */
    PREPARATION(1, "PREP", "草稿"),

    /**
     * 进行中
     */
    IN_PROGRESS(2, "IP", "进行中"),

    /**
     * 未完成
     */
    NOT_DONE(3, "ND", "未完成"),

    /**
     * 暂停
     */
    ON_HOLD(4, "OH", "暂停"),

    /**
     * 停止
     */
    STOPPED(5, "ST", "停止"),

    /**
     * 已完成
     */
    COMPLETED(6, "CMP", "已完成"),

    /**
     * 录入错误
     */
    ENTERED_IN_ERROR(7, "EIE", "录入错误"),

    /**
     * 未知
     */
    UNKNOWN(8, "UNK", "未知");

    private Integer value;
    private String code;
    private String info;

    public static EventStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (EventStatus val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
