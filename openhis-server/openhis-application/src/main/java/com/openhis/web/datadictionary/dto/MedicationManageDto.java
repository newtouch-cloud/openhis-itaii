package com.openhis.web.datadictionary.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import com.openhis.common.enums.PublicationStatus;

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
public class MedicationManageDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 药品编码 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long medicationDefId;

    /** 药品状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 所属科室 */
    @Dict(dictTable = "adm_organization",dictCode = "id",dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;
    private String orgId_dictText;

    /** 所在位置 */
    @Dict(dictTable = "adm_location",dictCode = "id",dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;
    private String locationId_dictText;

    /** 剂型 */
    @Dict(dictCode = "dose_form_code")
    private String doseFormCode;
    private String doseFormCode_dictText;

    /** 规格 */
    private String totalVolume;

    /** 成分 */
    private String ingredientItem;

    /** 是否为活性 */
    private Integer activeFlag;
    private String activeFlag_enumText;

    /** 批次号 */
    private String lotNumber;

    /** 生效日期 */
    private Date effectiveDate;

    /** 到期日期 */
    private Date expirationDate;

    /** 用法 */
    @Dict(dictCode = "method_code")
    private String methodCode;
    private String methodCode_dictText;

    /** 用药频次 */
    @Dict(dictCode = "rate_code")
    private String rateCode;
    private String rateCode_dictText;

    /** 单次剂量 */
    private BigDecimal dose;

    /** 剂量单位 */
    private String doseUnitCode;

    /** 单次最大剂量 */
    private BigDecimal maxUnit;

    /** 药品定义 */
    private String definition;

    /** 用量限定 */
    private BigDecimal usageLimit;

    /** DDD值 */
    private String dddCode;

    /** DDD单位 */
    @Dict(dictCode = "unit_code")
    private String dddUnitCode;
    private String dddUnitCode_dictText;

    /** 药品编号 */
    private String busNo;

    /** 药品名称 */
    private String name;

    /** 适用范围 */
    private Integer domainEnum;
    private String domainEnum_enumText;

    /** 药品版本 */
    private String version;

    /** 英文药名 */
    private String nameEn;

    /** 药品名称拼音码 */
    private String pyStr;

    /** 药品五笔码 */
    private String wbStr;

    /** 药品分类 */
    @Dict(dictCode = "med_category_code")
    private Integer categoryCode;
    private String categoryCode_dictText;

    /** 商品名称 */
    private String merchandiseName;

    /** 商品名称拼音码 */
    private String merchandisePyStr;

    /** 商品五笔码 */
    private String merchandiseWbStr;

    /** 药品单位 */
    private String unitCode;

    /** 最小单位 */
    @Dict(dictCode = "unit_code")
    private String minUnitCode;
    private String minUnitCode_dictText;

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
    @Dict(dictCode = "sys_yes_no")
    private Integer ybMatchFlag;
    private String ybMatchFlag_dictText;

    /** 医保编码 */
    private String ybNo;

    /** 药理作用分类 */
    private String pharmacologyCategoryCode;

    /** 是否皮试 */
    @Dict(dictCode = "sys_yes_no")
    private Integer skinTestFlag;
    private String skinTestFlag_dictText;

    /** 是否为注射药物 */
    @Dict(dictCode = "sys_yes_no")
    private Integer injectFlag;
    private String injectFlag_dictText;

    /** 生产厂家 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long manufacturerId;

    /** 供应商 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_supplier",dictCode = "id",dictText = "name")
    private Long supplyId;
    private String supplyId_dictText;

    /** 是否限制使用 */
    @Dict(dictCode = "sys_yes_no")
    private Integer restrictedFlag;
    private String restrictedFlag_dictText;

    /** 限制使用范围 */
    private String restrictedScope;

    /** 儿童用药标志 */
    @Dict(dictCode = "sys_yes_no")
    private Integer childrenFlag;
    private String childrenFlag_dictText;

    /** 产品特性 */
    private Integer characteristic;

    /** 贯标国家编码 */
    private String nationalDrugCode;

    /** 拆分属性 */
    private String partAttribute;

    /** 抗生素分类 */

    @Dict(dictCode = "antibiotic_type_code")
    private String antibioticCode;
    private String antibioticCode_dictText;


    /** 权限限制 */
    private Integer restrictedEnum;
    private String restrictedEnum_enumText;

    /** 是否自制 */
    @Dict(dictCode = "sys_yes_no")
    private Integer selfFlag;
    private String selfFlag_dictText;

    /** 是否抗生素 */
    @Dict(dictCode = "sys_yes_no")
    private Integer antibioticFlag;
    private String antibioticFlag_dictText;

    /** 基药标识 */
    @Dict(dictCode = "sys_yes_no")
    private Integer basicFlag;
    private String basicFlag_dictText;

    /** 生产厂家名称 */
    private String manufacturerName;

    /** 常规单位 */
    private String baseUnitCode;

    /** 当前库存数量(常规单位) */
    @Dict(dictCode = "unit_code")
    private String baseQuantity;
    private String baseQuantity_dictText;

    /** 当前库存数量(最小单位数量) */
    private String minQuantity;


}
