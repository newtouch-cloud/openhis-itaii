package com.openhis.web.datadictionary.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 药品目录Dto
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class MedicationManageUpDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 药品编码 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long medicationDefId;

    /** 所属科室 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;

    /** 所在位置 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /** 剂型 */
    private String doseFormCode;

    /** 规格 */
    private String totalVolume;

    /** 成分 */
    private String ingredientItem;

    /** 是否为活性 */
    private Integer activeFlag;

    /** 批次号 */
    private String lotNumber;

    /** 生效日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDate;

    /** 到期日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;

    /** 用法 */
    private String methodCode;

    /** 用药频次 */
    private String rateCode;

    /** 单次剂量 */
    private BigDecimal dose;

    /** 剂量单位 */
    @Dict(dictCode = "unit_code")
    private String doseUnitCode;
    private String doseUnitCode_dictText;

    /** 单次最大剂量 */
    private BigDecimal maxUnit;

    /** 药品定义 */
    private String definition;

    /** 药品编号 */
    @NotBlank(message = "药品编号不能为空")
    private String busNo;

    /** 药品名称 */
    @NotBlank(message = "药品名称不能为空")
    private String name;

    /** 适用范围 */
    private Integer domainEnum;

    /** 药品版本 */
    private String version;

    /** 英文药名 */
    private String nameEn;

    /** 药品名称拼音码 */
    private String pyStr;

    /** 药品五笔码 */
    private String wbStr;

    /** 药品分类 */
    private Integer categoryCode;

    /** 商品名称 */
    private String merchandiseName;

    /** 商品名称拼音码 */
    private String merchandisePyStr;

    /** 商品五笔码 */
    private String merchandiseWbStr;

    /** 药品单位 */
    private String unitCode;

    /** 最小单位 */
    private String minUnitCode;

    /** 所含耗材 */
    private String comprisedText;

    /** 成分 */
    private String ingredient;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 剂量形式 */
    private Integer doseFrom;

    /** 批准文号 */
    private String approvalNumber;

    /** 医保是否对码 */
    private Integer ybMatchFlag;

    /** 医保编码 */
    private String ybNo;

    /** 药理作用分类 */
    private String pharmacologyCategoryCode;

    /** 是否皮试 */
    private Integer skinTestFlag;

    /** 是否为注射药物 */
    private Integer injectFlag;

    /** 生产厂家 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long manufacturerId;

    /** 生产厂商文本 */
    private String manufacturerText;

    /** 供应商 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplyId;

    /** 是否限制使用 */
    private Integer restrictedFlag;

    /** 限制使用范围 */
    private String restrictedScope;

    /** 儿童用药标志 */
    private Integer childrenFlag;

    /** 产品特性 */
    private Integer characteristic;

    /** 购入价 */
    private BigDecimal purchasePrice;

    /** 零售价 */
    private BigDecimal retailPrice;

    /** 最高零售价 */
    private BigDecimal maximumRetailPrice;

    /** 医保类别 */
    private String ybType;

    /** 最小费用 */
    private String minimalFee;

    /** 单次最小用药频次 */
    private String minRateCode;

    /** 单次最大用药频次 */
    private String maxRateCode;

    /** 药品状态 */
    private Integer statusEnum;

    /** 拆分属性 */
    private String partAttribute;

    /** 贯标国家编码 */
    private String nationalDrugCode;

    /** 是否抗生素 */
    private Integer antibioticFlag;

    /** 是否自制 */
    private Integer selfFlag;

    /** 系统类别???? */

}
