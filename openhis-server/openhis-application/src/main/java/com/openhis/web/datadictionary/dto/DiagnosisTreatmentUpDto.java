package com.openhis.web.datadictionary.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import com.openhis.common.enums.ActivityDefCategory;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 诊疗目录分页更新
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiagnosisTreatmentUpDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 目录类别 */
    @NotNull(message = "目录类别不能为空")
    private String categoryCode;

    /** 编码 */
    @NotBlank(message = "项目编码不能为空")
    private String busNo;

    /** 项目名称 */
    @NotBlank(message = "项目名称不能为空")
    private String name;

    /** 项目名称拼音 */
    @NotBlank(message = "项目名称拼音不能为空")
    private String pyStr;

    /** 五笔拼音 */
    @NotBlank(message = "五笔拼音不能为空")
    private String wbStr;

    /** 类型 */
    @NotBlank(message = "类型不能为空")
    private String typeCode;

    /** 使用单位 */
    @NotBlank(message = "使用单位不能为空")
    private String permittedUnitCode;

    /** 医保标记 */
    // @NotNull(message = "医保标记不能为空")
    private Integer ybFlag;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    // @NotNull(message = "医保对码标记不能为空")
    private Integer ybMatchFlag;

    /** 身体部位 */
    private String bodySiteCode;

    /** 所需标本 */
    private String specimenCode;

    /** 说明 */
    private String descriptionText;

    /** 规则id */
    private Integer ruleId;

    /** 所属科室 */
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;
    private String orgId_dictText;

    /** 所在位置 */
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;
    private String locationId_dictText;

    /** 财务类别 */
    @Dict(dictCode = "fin_type_code")
    private String itemTypeCode;
    private String itemTypeCode_dictText;

    /** 医保类别 */
    @Dict(dictCode = "yb_type")
    private String ybType;
    private String ybType_dictText;

    /** 购入价 */
    private BigDecimal purchasePrice;

    /** 零售价 */
    private BigDecimal retailPrice;

    /** 最高零售价 */
    private BigDecimal maximumRetailPrice;

}
