/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医院审批标志
 *
 * @author SunJQ
 * @date 2025-04-14
 */
@Getter
@AllArgsConstructor
public enum YbHospApprFlag {

    /**
     * 无须审批
     */
    NO_APPROVAL_REQUIRED("0", "无须审批"),
    /**
     * 审批不通过
     */
    APPROVAL_NOT_PASSED("2", "审批不通过"),
    /**
     * 审批通过
     */
    APPROVAL_PASSED("1", "审批通过");

    private String value;
    private String description;

    public static YbHospApprFlag getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbHospApprFlag val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
