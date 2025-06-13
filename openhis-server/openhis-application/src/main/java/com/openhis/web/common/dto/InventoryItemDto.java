/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.common.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 库存项目dto
 *
 * @author zwh
 * @date 2025-04-01
 */
@Data
@Accessors(chain = true)
public class InventoryItemDto {

    /** 项目类型 */
    private Integer itemType; // 1:药品 , 2: 耗材 , 3:诊疗
    private String itemType_enumText;

    /**
     * 药品/耗材类型
     */
    private String categoryCode;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 采购单价 */
    private BigDecimal purchaseAmount;

    /** 项目定义ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionId;

    /** 项目名称 */
    private String name;

    /**
     * 项目名称
     */
    private String itemBusNo;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 医保编码 */
    private String ybNo;

    /** 商品名称 */
    private String productName;

    /** 包装单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 最小单位 */
    @Dict(dictCode = "unit_code")
    private String minUnitCode;
    private String minUnitCode_dictText;

    /**
     * 规格
     */
    private String volume;

    /**
     * 供应商名称
     */
    private String supplier;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 项目对应表名
     */
    private String itemTableName;

    /**
     * 产品批号
     */
    private String lotNumber;

    /** 原仓库 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgLocation;

    /**
     * 单位列表
     */
    private List<UnitDto> unitList;
}
