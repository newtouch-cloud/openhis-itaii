package com.openhis.web.datadictionary.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import com.openhis.common.enums.ActivityDefCategory;
import com.openhis.common.enums.PublicationStatus;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 诊疗目录分页检索
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiagnosisTreatmentDto {
    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 目录类别 */
    @Dict(dictCode = "activity_category_code")
    private String categoryCode;
    private String categoryCode_dictText;

    /** 编码 */
    private String busNo;

    /** 项目名称 */
    private String name;

    /** 项目名称拼音 */
    private String pyStr;

    /** 五笔拼音 */
    private String wbStr;

    /** 类型 */
    private Integer typeEnum;
    private String typeEnum_enumText;

    /** 使用单位 */
    @Dict(dictCode = "unit_code")
    private String permittedUnitCode;
    private String permittedUnitCode_dictText;

    /** 医保标记 */
    private Integer ybFlag;
    private String ybFlag_enumText;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    private Integer ybMatchFlag;
    private String ybMatchFlag_enumText;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 身体部位 */
    @Dict(dictCode = "body_site_code")
    private String bodySiteCode;
    private String bodySiteCode_dictText;

    /** 所需标本 */
    @Dict(dictCode = "specimen_code")
    private String specimenCode;
    private String specimenCode_dictText;

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
    @Dict(dictCode = "med_chrgitm_type")
    private String ybType;
    private String ybType_dictText;

//    /** 购入价 */
//    private BigDecimal purchasePrice;

    /** 零售价 */
    private BigDecimal retailPrice;

    /** 最高零售价 */
    private BigDecimal maximumRetailPrice;

    /** 医保等级 */
    private Integer chrgitmLv;

    /** 子项json */
    private String childrenJson;

    /** 划价标记 */
    private Integer pricingFlag;
    private String pricingFlag_enumText;

}
