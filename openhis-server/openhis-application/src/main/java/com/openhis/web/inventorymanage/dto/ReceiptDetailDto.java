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
 * 单据详情
 *
 * @author zwh
 * @date 2025-03-04
 */
@Data
@Accessors(chain = true)
public class ReceiptDetailDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String busNo;

    /** 当前库存总数 */
    private BigDecimal totalQuantity;

    /** 数量 */
    private BigDecimal itemQuantity;

    /** 所在表 */
    private String itemTable;

    /** 药品类型 */
    private String categoryCode;

    /** 物品编码 */
    private String itemBusNo;

    /** 药品类型 */
    private String itemType;

    /** 物品名称 */
    private String itemName;

    /** 物品id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    /** 规格 */
    private String totalVolume;

    /** 厂家/产地 */
    private String manufacturerText;

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

    /** 请求细节 */
    private String detailJson;

    /** 供应人名称 */
    private String practitionerName;

    /** 供应商id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;

    /** 目的仓库名称 */
    private String purposeLocationName;

    /** 目的仓库id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long purposeLocationId;

    /** 目的货位id */
    private Long purposeLocationStoreId;

    /** 目的货位名称 */
    private String purposeLocationStoreName;

    /** 申请时间 */
    private Date applyTime;

    /** 盘点时间 */
    private Date occurrenceTime;

    /** 产品批号 */
    private String lotNumber;

    /** 追溯码 */
    private String traceNo;

    /** 发票号 */
    private String invoiceNo;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 单价 */
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 盈亏原因类型 */
    private String reasonCode;

    /** 盈亏原因 */
    private String reason;

    /** 备注 */
    private String remake;

    /** 医保编码 */
    private String ybNo;

    /** 拆零比 */
    private String partPercent;

    /**
     * 单位列表
     */
    private List<UnitDto> unitList;
}
