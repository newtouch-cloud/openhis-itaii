/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 处方发药单
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class PrescriptionMedicineInfoDto {

    /**
     * 发放药房
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /**
     * 药品id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long medicineId;

    /**
     * 发放id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dispenseId;

    /**
     * 请求id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requestId;

    /**
     * 发放药房
     */
    private String locationName;

    /**
     * 批次号
     */
    private String lotNumber;

    /**
     * 科室
     */
    private String departmentName;

    /**
     * 开单医生
     */
    private String doctorName;

    /**
     * 发药医生
     */
    private String dispenseDoctorName;

    /**
     * 配药医生
     */
    private String preparerDoctorName;

    /**
     * 项目类型
     */
    @Dict(dictCode = "med_category_code")
    private String itemType;
    private String itemType_dictText;

    /**
     * 发药状态
     */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /**
     * 诊断名称
     */
    private String conditionName;

    /**
     * 处方号
     */
    private String prescriptionNo;

    /**
     * 药品名称
     */
    private String medicineName;

    /**
     * 规格
     */
    private String totalVolume;

    /**
     * 单次剂量
     */
    private BigDecimal dose;

    /**
     * 用药频次
     */
    private String rateCode;

    /**
     * 用法
     */
    @Dict(dictCode = "method_code")
    private String methodCode;
    private String methodCode_dictText;

    /**
     * 剂量单位
     */
    @Dict(dictCode = "unit_code")
    private String doseUnitCode;
    private String doseUnitCode_dictText;

    /**
     * 单位
     */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /**
     * 单次最大剂量
     */
    private BigDecimal maxDose;

    /**
     * 首次用量
     */
    private BigDecimal firstDose;

    /**
     * 首次持续时间
     */
    private String firstDuration;

    /**
     * 给药间隔
     */
    private String dispenseInterval;

    /**
     * 单次发药数
     */
    private Integer dispensePerQuantity;

    /**
     * 每次发药供应天数
     */
    private Integer dispensePerDuration;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    private BigDecimal totalPrice;
    /**
     * 组合号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;
}
