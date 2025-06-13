/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 7.35 就诊凭证类型(mdtrt_cert_type)
 *
 * @author SunJQ
 * @date 2025-04-10
 */
@Getter
@AllArgsConstructor
public enum YbMdtrtCertType {

    /**
     * 医保电子凭证
     */
    MDTRT_CERT_TYPE01("01","1","医保电子凭证"),
    /**
     * 居民身份证
     */
    MDTRT_CERT_TYPE02("02","2","居民身份证"),
    /**
     * 社会保障卡
     */
    MDTRT_CERT_TYPE03("03","3","社会保障卡");

    private String value;
    private String code;//2025/05/20,由于account数据库中存的1/2/3，无法与医保码01，02，03直接对照，故此增加code字段比较
    private String description;

    public static YbMdtrtCertType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbMdtrtCertType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }

    public static YbMdtrtCertType getByCode(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbMdtrtCertType val : values()) {
            if (val.getCode().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
