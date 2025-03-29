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
    ENCOUNTER_NUM("3", "就诊编号", "EN"),

    /**
     * 处方号
     */
    PRESCRIPTION_NO("4", "处方号", "P"),

    /**
     * 药品请求编码
     */
    MEDICATION_RES_NO("5", "药品请求编码", "MR"),

    /**
     * 耗材请求编码
     */
    DEVICE_RES_NO("6", "耗材请求编码", "DR"),

    /**
     * 服务项目请求编码
     */
    SERVICE_RES_NO("7", "服务项目请求编码", "SR"),

    /**
     * 费用项目编码
     */
    CHARGE_ITEM_NO("8", "费用项目编码", "CI"),

    /**
     * 药品项目编码
     */
    MEDICATION_NUM("9", "药品项目编码", "MD"),

    /**
     * 器材项目编码
     */
    DEVICE_NUM("10", "项目编码", "DD"),

    /**
     * 支付编码
     */
    PAYMENT_NO("11", "支付编码", "SF");

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