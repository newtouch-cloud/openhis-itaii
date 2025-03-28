/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 就诊人员列表
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class PrescriptionMedicineInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 科室 */
    private String departmentName;

    /** 项目类型 */
    private String itemType;

    /** 开单医生 */
    private String doctorName;

    /** 处方号 */
    private String prescriptionNo;

    /** 药品名称 */
    private String medicineName;

    /** 规格 */
    private String totalVolume;

    /** 单次剂量 */
    private BigDecimal dose;

    /** 用药频次 */
    private String rateCode;

    /** 用法 */
    private String methodCode;

    /** 剂量单位 */
    private String doseUnitCode;

    /** 单次最大剂量 */
    private BigDecimal maxDose;

    /** 首次用量 */
    private BigDecimal firstDose;

    /** 首次持续时间 */
    private String firstDuration;

    /** 给药间隔 */
    private String dispenseInterval;

    /** 单次发药数 */
    private Integer dispensePerQuantity;

    /** 每次发药供应天数 */
    private Integer dispensePerDuration;

    /** 数量 */
    private Integer quantity;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal total_price;
}