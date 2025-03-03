package com.openhis.web.datadictionary.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.DeviceCategory;
import com.openhis.common.enums.PublicationStatus;

import lombok.Data;
import lombok.experimental.Accessors;

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
    private DeviceCategory categoryEnum;

    /** 器材种类 */
    private String typeCode;

    /** 包装单位 */
    private String unitCode;

    /** 包装规格 */
    private String size;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 最小使用单位 */
    private String minUnitCode;

    /** 产品型号 */
    private String modelNumber;

    /** 高值器材标志 */
    private Integer hvcmFlag;
    private String hvcmFlag_enumText;

    /** 销售单位 */
    private String salesUnitCode;

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
    private PublicationStatus statusEnum;

    /** 生产厂家 */
    private Long manufacturerId;

    /** 供应商 */
    private Long supplyId;

    /** 说明 */
    private String description;

    /** 适用范围 */
    private String jurisdiction;

    /** 执行科室 */
    private Long ruleId;

    /** 器材版本 */
    private String version;

    /** 主要成分 */
    private String substanceText;

    /** 过敏标记 */
    private Integer allergenFlag;
    private String allergenFlag_enumText;
}
