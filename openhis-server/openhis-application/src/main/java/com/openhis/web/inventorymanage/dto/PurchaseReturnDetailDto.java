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
 * 退货单据详情
 *
 * @author yuanzs
 * @date 2025-04-02
 */
@Data
@Accessors(chain = true)
public class PurchaseReturnDetailDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 采购入库ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long purchaseId;

    /** 单据号 */
    private String busNo;

    /** 原始单据号 */
    private String originalBusNo;

    /** 退库数量（数量） */
    private BigDecimal itemQuantity;

    /** 已退数量 */
    private BigDecimal returnedQuantity;

    /** 当前库存总数 */
    private BigDecimal quantity;

    /** 采购数量 */
    private BigDecimal purchaseQuantity;

    /** 采购单价 */
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 采购单位 */
    @Dict(dictCode = "unit_code")
    private String purchaseUnitCode;
    private String purchaseUnitCode_dictText;

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

    /** 目的仓库名称 */
    private String purposeLocationName;

    /** 目的货位名称 */
    private String purposeLocationStoreName;

    /** 物品编码 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    /** 物品名称 */
    private String itemName;

    /** 项目 */
    private String itemTable;

    /** 规格 */
    private String totalVolume;

    /** 生产厂商 */
    private String manufacturer;

    /** 产品批号 */
    private String lotNumber;

    /** 备注 */
    private String remake;

    /** 退库原因 */
    private String reason;

    /** 目的仓库ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long purposeLocationId;

    /** 目的货位ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long purposeLocationStoreId;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 经办人 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;

    /** 供应商id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 药品类型 */
    private Integer itemType; // 1:药品 , 2: 耗材 , 3:诊疗
    private String itemType_enumText;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;
    private String purposeTypeEnum_dictText;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 发票号 */
    private String invoiceNo;

    /** 追溯码 */
    private String traceNo;

    /** 追溯码包装层级 */
    private String packagingLevels;

    /**
     * 单位列表
     */
    private List<UnitDto> unitList;

}
