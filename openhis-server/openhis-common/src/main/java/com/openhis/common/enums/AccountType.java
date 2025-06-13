/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.core.common.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户类型
 *
 * @author zwh
 * @date 2025-03-14
 */
@Getter
@AllArgsConstructor
public enum AccountType implements HisEnumInterface {

    /**
     * 医保电子凭证
     */
    MEDICAL_ELECTRONIC_CERTIFICATE(1, "01", "医保电子凭证"),

    /**
     * 居民身份证
     */
    ID_CARD(2, "02", "居民身份证"),

    /**
     * 社会保障卡
     */
    SOCIAL_SECURITY_CARD(3, "03", "社会保障卡"),

    /**
     * 个人现金账户
     */
    PERSONAL_CASH_ACCOUNT(4, "04", "个人现金账户");

    private Integer value;
    private String code;
    private String info;

    public static AccountType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (AccountType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }

    public static AccountType getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (AccountType val : values()) {
            if (val.getCode().equals(code)) {
                return val;
            }
        }
        return null;
    }
}
