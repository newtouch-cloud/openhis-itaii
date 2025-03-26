package com.openhis.web.datadictionary.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.DeviceCategory;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 器材目录更新
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DeviceManageUpDto {
    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编码 */
    @NotBlank(message = "器材编码不能为空")
    private String busNo;

    /** 器材名称 */
    @NotBlank(message = "器材名称不能为空")
    private String name;

    /** 器材名称拼音 */
    @NotBlank(message = "器材名称拼音不能为空")
    private String pyStr;

    /** 器材五笔拼音 */
    @NotBlank(message = "器材五笔拼音不能为空")
    private String wbStr;

    /** 器材分类 */
    @NotNull(message = "器材分类不能为空")
//    private DeviceCategory categoryEnum;
    private Integer categoryEnum;
    private String categoryEnum_enumText;


    /** 器材种类 */
    @NotBlank(message = "器材种类不能为空")
    private String typeCode;

    /** 包装单位 */
    @NotBlank(message = "包装单位不能为空")
    private String unitCode;

    /** 包装规格 */
    @NotBlank(message = "包装规格不能为空")
    private String size;

    /** 拆零比 */
    @NotNull(message = "拆零比不能为空")
    private BigDecimal partPercent;

    /** 最小使用单位 */
    @NotBlank(message = "最小使用单位不能为空")
    private String minUnitCode;

    /** 产品型号 */
    @NotBlank(message = "产品型号不能为空")
    private String modelNumber;

    /** 高值器材标志 */
    // @NotNull(message = "高值器材标志不能为空")
    private Integer hvcmFlag;

    /** 销售单位 */
    @NotBlank(message = "销售单位不能为空")
    private String salesUnitCode;

    /** 批准文号 */
    @NotBlank(message = "批准文号不能为空")
    private String approvalNumber;

    /** 医保标记 */
    // @NotNull(message = "医保标记不能为空")
    private Integer ybFlag;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    // @NotNull(message = "医保对码标记不能为空")
    private Integer ybMatchFlag;

    /** 生产厂家 */
    @NotNull(message = "生产厂家不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long manufacturerId;

    /** 供应商 */
    @NotNull(message = "供应商不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplyId;

    /** 说明 */
    private String description;

    /** 适用范围 */
    @NotBlank(message = "适用范围不能为空")
    private String jurisdiction;

    /** 执行科室 */
    @NotNull(message = "执行科室不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ruleId;

    /** 归属科室 */
    @NotNull(message = "归属科室不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;

    /** 所在位置 */
    @NotNull(message = "所在位置不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /** 器材版本 */
    private String version;

    /** 主要成分 */
    private String substanceText;

    /** 过敏标记 */
    // @NotNull(message = "过敏标记不能为空")
    private Integer allergenFlag;

    /** 购入价 */
    @NotNull(message = "购入价不能为空")
    private BigDecimal purchasePrice;

    /** 零售价 */
    @NotNull(message = "零售价不能为空")
    private BigDecimal retailPrice;

    /** 最高零售价 */
    @NotNull(message = "最高零售价不能为空")
    private BigDecimal maximumRetailPrice;

    /** 最小费用 */
    private String minimalFee;

    /** 医保类别 */
    private String ybType;

}
