package com.openhis.web.doctorstation.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 药品信息 dto
 */
@Data
@Accessors(chain = true)
public class ElepMedicationInfoDto {

    private Long id;

    /** 药品定义id */
    private String medicationId;
    /** 药品名 */
    private String medicationName;
    /** 药品规格 */
    private String drugSpecification;
    /** 生产厂家 */
    private String manufacturerName;
    /** 药品剂量 */
    private BigDecimal medDosage;
    /** 药品剂量单位 */
    private String medDosageUnitCode;
    /** 使用频次 */
    @Dict(dictCode = "elep_rate_code")
    private String medFrequency;
    private String medFrequency_dictText;
    /** 途径 */
    @Dict(dictCode = "method_code")
    private String medRoute;
    private String medRoute_dictText;
    /** 请求数量 */
    private Integer quantity;

    /** 请求单位 */
    private String unitCode;

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
    /** 处方类别 */
    private Integer rxTypeCode;
}
