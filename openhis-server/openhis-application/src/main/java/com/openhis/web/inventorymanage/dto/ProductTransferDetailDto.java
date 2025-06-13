/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import com.openhis.web.common.dto.UnitDto;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 调拨单据详情
 *
 * @author zwh
 * @date 2025-03-04
 */
@Data
@Accessors(chain = true)
public class  ProductTransferDetailDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String busNo;

    /** 申请时间 */
    private Date applyTime;

    /** 源仓库类型 */
    private Integer sourceTypeEnum;
    private String sourceTypeEnum_dictText;

    /** 源仓库id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sourceLocationId;

    /** 源仓库名称 */
    private String sourceLocationName;

    /** 源货位id */
    private Long sourceLocationStoreId;

    /** 源货位名称 */
    private String sourceLocationStoreName;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;
    private String purposeTypeEnum_dictText;

    /** 目的仓库id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long purposeLocationId;

    /** 目的仓库名称 */
    private String purposeLocationName;

    /** 目的货位id */
    private Long purposeLocationStoreId;

    /** 目的货位名称 */
    private String purposeLocationStoreName;

    /** 项目类型 */
    private Integer itemType; // 1:药品 , 2: 耗材 , 3:诊疗
    private String itemType_enumText;

    /** 项目(药品名称) */
    private String name;

    /** 规格 */
    private String totalVolume;

    /** 生产厂商 */
    private String manufacturer;

    /** 供应商id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 物品计量单位 */
    @Dict(dictCode = "unit_code")
    private String measurementUnitCode;
    private String measurementUnitCode_dictText;

    /** 包装单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 最小单位 */
    @Dict(dictCode = "unit_code")
    private String minUnitCode;
    private String minUnitCode_dictText;

    /** 数量 */
    private BigDecimal itemQuantity;

    /** 源库存数量 */
    private BigDecimal totalSourceQuantity;

    /** 目的库存数量 */
    private BigDecimal totalPurposeQuantity;

    /** 单价 */
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 产品批号 */
    private String lotNumber;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 追溯码 */
    private String traceNo;

    /** 备注 */
    private String remake;

    /** 追溯码包装层级 */
    private String packagingLevels;

    /** 项目id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    /** 拆零比 */
    private String partPercent;

    /**
     * 单位列表
     */
    private List<UnitDto> unitList;
}
