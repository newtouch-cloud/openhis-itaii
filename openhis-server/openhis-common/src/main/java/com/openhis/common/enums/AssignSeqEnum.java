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
    PURCHASE_NUM("2", "采购单据号", "PUR"),

    /**
     * 就诊编号
     */
    ENCOUNTER_NUM("3", "就诊编号", "EN"),

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
    PAYMENT_NO("11", "支付编码", "SF"),

    /**
     * 诊疗项目编码
     */
    ACTIVITY_DEFINITION_NUM("11", "诊疗编码", "AC"),

    /**
     * 诊断定义编码
     */
    CONDITION_DEFINITION_NUM("11", "诊断编码", "CD"),

    /**
     * 科室业务编码
     */
    ORGANIZATION_BUS_NO("14", "科室业务编码", "ORG"),

    /**
     * 位置业务编码
     */
    LOCATION_BUS_NO("15", "科室业务编码", "LOC"),

    /**
     * 厂商/产地单据号
     */
    SUPPLIER_BUS_NO("16", "供应商编号", "SUP"),

    /**
     * 盘点单据号
     */
    STOCKTAKING_NUM("17", "盘点单据号", "STO"),
	
    /**
     * 报损单单据号
     */
    LOSS_BUS_NO("18", "报损单编号", "LOS"),

    /**
     * 采购退货单据号
     */
    RETURN_BUS_NO("17", "退货编号", "RET"),

    /**
     * 领用出库单据号
     */
    REQUISITION_NUM("19", "领用出库单据号", "REQ"),

    /**
     * 退货出库单据号
     */
    RETURN_ISSUE_NUM("20", "退货出库单据号", "RIS"),

    /**
     * 处方号-通用药物
     */
    PRESCRIPTION_COMMON_NO("21", "处方号-通用药物", "PCN"),

    /**
     * 处方号-麻醉药品
     */
    PRESCRIPTION_NARCOTIC_NO("22", "处方号-麻醉药品", "PNN"),

    /**
     * 处方号-毒性药品
     */
    PRESCRIPTION_TOXIC_NO("23", "处方号", "PTN"),

    /**
     * 处方号-一类精神药
     */
    PRESCRIPTION_A_PSYCHOTROPIC_NO("24", "处方号", "PAN"),

    /**
     * 处方号-二类精神药
     */
    PRESCRIPTION_B_PSYCHOTROPIC_NO("25", "处方号", "PBN"),
    
    /**
     * 住院编号
     */
    ADMISSION_NUM("26", "住院号", "ZY"),

    /**
     * 调拨单据号
     */
    TRANSFER_NUM("27", "调拨单据号", "TRA"),
    /**
     * 发票单据号
     */
    INVOICE_NUM("28", "发票单据号", "INV"),

    /**
     * 药品发放编码
     */
    MEDICATION_DIS_NO("29", "药品发放编码", "MS"),

    /**
     * 耗材发放编码
     */
    DEVICE_DIS_NO("30", "耗材发放编码", "DS"),

    /**
     * 医院内部处方编号
     */
    ELEP_MEDICATION_NO("31", "医院内部处方编号", "ER"),

    /**
     * 医保收费批号
     */
    YB_CLINIC_ORDER("32", "医保订单编号", "YBORD"),

    /**
     * 医保收费批号
     */
    YB_CLINIC_FEE("34", "费用明细流水号", "F"),

    /**
     * 自费收费批号
     */
    SF_CLINIC_ORDER("33", "自费订单编号", "SFORD"),;

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