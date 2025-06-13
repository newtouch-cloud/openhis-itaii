package com.openhis.web.datadictionary.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import com.openhis.common.enums.DeviceCategory;
import com.openhis.common.enums.PublicationStatus;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 器材目录分页检索
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DeviceManageDto {
    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编码 */
    private String busNo;

    /** 器材名称 */
    private String name;

    /** 器材名称拼音 */
    private String pyStr;

    /** 器材五笔拼音 */
    private String wbStr;

    /** 器材分类 */
    @Dict(dictCode = "device_category_code")
    private String categoryCode;
    private String categoryCode_dictText;

    /** 器材种类 */
    @Dict(dictCode = "device_type_code")
    private String typeCode;
    private String typeCode_dictText;

    /** 包装单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 包装规格 */
    private String size;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 最小使用单位 */
    @Dict(dictCode = "unit_code")
    private String minUnitCode;
    private String minUnitCode_dictText;

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

    /** 产品型号 */
    private String modelNumber;

    /** 高值器材标志 */
    private Integer hvcmFlag;
    private String hvcmFlag_enumText;

    /** 销售单位 */
    @Dict(dictCode = "unit_code")
    private String salesUnitCode;
    private String salesUnitCode_dictText;

    /** 批准文号 */
    private String approvalNumber;

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

    /** 生产厂家 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long manufacturerId;

    /** 生产厂商文本 */
    private String manufacturerText;

    /** 供应商 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_supplier",dictCode = "id",dictText = "name")
    private Long supplyId;
    private String supplyId_dictText;

    /** 说明 */
    private String description;

    /** 适用范围 */
    private String jurisdiction;

    /** 器材版本 */
    private String version;

    /** 主要成分 */
    private String substanceText;

    /** 过敏标记 */
    private Integer allergenFlag;
    private String allergenFlag_enumText;

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

    /** 医保等级 */
    private Integer chrgitmLv;

}
