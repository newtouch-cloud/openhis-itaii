/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 医保电子处方信息
 *
 * @author yuxj
 * @date 2025-05-06
 */
@Data
@Accessors(chain = true)
public class VeriPrescriptionDetailInfoDto {

    /** 处方号 */
    private String prescriptionNo;
    /** 门诊号 */
    private String iptOtpNo;
    /** 请求数量 */
    private Integer quantity;
    /** 请求单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;
    /** 审核状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /* --处方信息-- */
    /** 药品名 */
    private String medicationName;
    /** 药品规格 */
    private String drugSpecification;
    /** 药品剂量 */
    private BigDecimal medDosage;
    /** 药品剂量单位 */
    @Dict(dictCode = "unit_code")
    private String medDosageUnitCode;
    private String medDosageUnitCode_dictText;
    /** 使用频次 */
    @Dict(dictCode = "rate_code")
    private String medFrequency;
    private String medFrequency_dictText;
    /** 途径 */
    @Dict(dictCode = "method_code")
    private String medRoute;
    private String medRoute_dictText;
    /** 取药状态 */
    private String medStatus;
    /** 处方状态 */
    private String prescriptionStatus;
    /** 处方类别 */
    private Integer rxTypeCode;
    private String rxTypeCode_enumText;
    /** 支持用药信息 */
    private String supportInfo;
    /** 服药时间(开始) */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDoseStart;
    /** 服药时间(结束) */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDoseEnd;
    /** 给药间隔 */
    private String dispenseInterval;
    /** 单次发药数 */
    private Integer dispensePerQuantity;
    /** 每次发药供应天数 */
    private Integer dispensePerDuration;

    /* --就诊信息-- */
    /** 患者姓名 */
    private String patnName;
    /** 身份证号 */
    private String certno;
    /** 开方医生名 */
    private String practitionerName;
    /** 挂号科室 */
    private String mdtrtDeptName;
    /** 开单科室 */
    private String prscDeptName;
    /** 挂号日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mdtrtTime;
    /** 处方开立日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date prscTime;

    /* --诊断信息-- */
    /** 诊断名 */
    private String conditionName;

}
