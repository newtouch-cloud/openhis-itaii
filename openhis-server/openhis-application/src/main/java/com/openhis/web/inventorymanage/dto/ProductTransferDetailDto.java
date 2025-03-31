/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 调拨单据详情
 *
 * @author zwh
 * @date 2025-03-04
 */
@Data
@Accessors(chain = true)
public class ProductTransferDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /** 源仓库名称 */
    private String sourceLocationName;

    /** 源货位名称 */
    private String sourceLocationStoreName;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;
    private String purposeTypeEnum_dictText;

    /** 目的仓库名称 */
    private String purposeLocationName;

    /** 目的货位名称 */
    private String purposeLocationStoreName;

    /** 项目(药品类型) */
    private String itemTable;

    /** 规格 */
    private String totalVolume;

    /** 供应商名称 */
    private String supplierName;

    /** 物品计量单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

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

    /** 理由 */
    private String reason;

    /** 售价 */
    private BigDecimal sellPrice;

    /** 拆零售价 */
    private BigDecimal minSellPrice;

}
