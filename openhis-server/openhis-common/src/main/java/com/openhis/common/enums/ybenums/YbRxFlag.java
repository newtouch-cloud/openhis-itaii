/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 处方标记【rx_flag】
 *
 * @author SunJQ
 * @date 2025-04-17
 */
@Getter
@AllArgsConstructor
public enum YbRxFlag {

    // 否
    WESTERN_AND_CHINESE_PATENT_MEDICINE(0, "0","否"),
    // 是
    IMPORTANT_HERBAL_SLICES(1, "1","是"),
    // 双跨
    SELF_PREPARED_MEDICATION(2, "2","双跨");

    private final int code;
    private final String value;
    private final String name;

    public static YbRxFlag getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbRxFlag val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
