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
public enum DispenseStatusEnum implements HisEnumInterface {

    /**
     * 草稿
     */
    DRAFT(0, "DR", "草稿"),

    /**
     * 待配药
     */
    PREPARATION(1, "PR", "待配药"),

    /**
     * 待发药
     */
    IN_PROGRESS(2, "IN", "待发药"),

    /**
     * 已发药
     */
    COMPLETED(3, "CO", "已发药"),

    /**
     * 暂停
     */
    ON_HOLD(4, "ON", "暂停"),

    /**
     * 停止
     */
    STOPPED(5, "ST", "停止"),

    /**
     * 未知
     */
    UNKNOWN(6, "UN", "未知"),

    /**
     * 拒绝发药 已汇总，部分发药，部分退药，已退药
     */
    DECLINED(7, "DE", "拒绝发药 已汇总，部分发药，部分退药，已退药");

    private Integer value;
    private String code;
    private String info;

    public static DispenseStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (DispenseStatusEnum val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
