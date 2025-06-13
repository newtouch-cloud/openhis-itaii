/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医保个人结算方式
 *
 * @author SunJQ
 * @date 2025-04-14
 */
@Getter
@AllArgsConstructor
public enum YbPsnSetlWay {

    /**
     * 按项目结算
     */
    PSN_SETLWAY01("01", "按项目结算"),
    /**
     * 按定额结算
     */
    PSN_SETLWAY02("02", "按定额结算");

    private String value;
    private String description;

    public static YbPsnSetlWay getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbPsnSetlWay val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
