/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

/**
 * 采番前缀枚举
 *
 * @author zxy
 * @date 2025-02-24
 */
public enum AssignSeqEnum {

    /**
     * 患者编号
     */
    PATIENT_NUM("1", "患者编号", "PN"),

    /**
     * 采购单据号
     */
    PURCHASE_NUM("2", "采购单据号", "SIN"),

    /**
     * 就诊编号
     */
    ENCOUNTER_NUM("3", "就诊编号", "EN");

    private final String code;
    private final String info;
    private final String prefix;

    AssignSeqEnum(String code, String info, String prefix) {
        this.code = code;
        this.info = info;
        this.prefix = prefix;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getPrefix() {
        return prefix;
    }
}