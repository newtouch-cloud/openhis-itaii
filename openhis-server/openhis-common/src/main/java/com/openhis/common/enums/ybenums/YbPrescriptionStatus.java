package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医保电子处方状态枚举
 *
 * @author liuhr
 * @date 2025-04-22
 */
@Getter
@AllArgsConstructor
public enum YbPrescriptionStatus {

    /**
     * 有效
     */
    VALID("1", "有效"),

    /**
     * 已失效
     */
    EXPIRED("2", "已失效"),

    /**
     * 已撤销
     */
    REVOKED("3", "已撤销"),

    /**
     * 已作废
     */
    INVALID("4", "已作废");

    private String value;
    private String description;

    public static YbPrescriptionStatus getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbPrescriptionStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}