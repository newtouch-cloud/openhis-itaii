/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * YbPayLoc 支付地点 PAY_LOC  报销标志 REIM_FLAG
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Getter
@AllArgsConstructor
public enum YbPayLoc {

    /**
     * 中心
     */
    PAY_LOC1("1", "中心"),
    /**
     * 医疗机构
     */
    PAY_LOC2("2", "医疗机构"),
    /**
     * 省内异地
     */
    PAY_LOC3("3", "省内异地"),
    /**
     * 跨省异地
     */
    PAY_LOC4("4", "跨省异地"),
    /**
     * 互联网医院
     */
    PAY_LOC5("5", "互联网医院");

    private String value;
    private String description;

    public static YbPayLoc getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbPayLoc val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
