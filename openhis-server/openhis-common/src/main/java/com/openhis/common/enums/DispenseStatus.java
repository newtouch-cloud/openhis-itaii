package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药品请求状态
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Getter
@AllArgsConstructor
public enum DispenseStatus implements HisEnumInterface {

    /**
     * 草稿
     */
    DRAFT(1, "DR", "草稿"),

    /**
     * 待配药
     */
    PREPARATION(2, "PR", "待配药"),

    /**
     * 待发药
     */
    IN_PROGRESS(3, "IN", "待发药"),

    /**
     * 已发药
     */
    COMPLETED(4, "CO", "已发药"),

    /**
     * 暂停
     */
    ON_HOLD(5, "ON", "暂停"),

    /**
     * 停止
     */
    STOPPED(6, "ST", "停止"),

    /**
     * 拒绝发药
     */
    DECLINED(7, "DE", "拒绝发药"),

    /**
     * 已汇总
     */
    SUMMARIZED(8, "SR", "已汇总"),

    /**
     * 部分发药
     */
    PART_COMPLETED(9, "PC", "部分发药"),

    /**
     * 部分退药
     */
    PART_REFUND(10, "PR", "部分退药"),

    /**
     * 退药中
     */
    IN_REFUND(11, "IR", "退药中"),

    /**
     * 已退药
     */
    REFUNDED(12, "RE", "已退药"),

    /**
     * 未知
     */
    UNKNOWN(13, "UN", "未知");

    private Integer value;
    private String code;
    private String info;

    public static DispenseStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (DispenseStatus val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
