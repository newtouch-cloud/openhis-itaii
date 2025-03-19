/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 入库单据
 *
 * @author ZhangYC
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class InventoryReceiptDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 项目 */
    private String itemTable;

    /** 数量 */
    @NotNull
    @Min(1)
    private Integer itemQuantity;

    /** 物品编码 */
    @NotNull
    private Long itemId;

    /** 物品计量单位 */
    @NotNull
    private String unitCode;

    /** 请求细节 */
    private String detailJson;

    /** 供应商 */
    @NotNull
    private Long supplierId;

    /** 目的仓库类型 */
    @NotNull
    private Integer purposeTypeEnum;

    /** 目的仓库 */
    @NotNull
    private Long purposeLocationId;

    /** 目的仓位 */
    @NotNull
    private Long purposeLocationStoreId;

    /** 申请人 */
    @NotNull
    private Long applicantId;

    /** 经手人 */
    @NotNull
    private Long practitionerId;

    /** 申请时间 */
    private Date applyTime;

    /** 产品批号 */
    @NotNull
    private String lotNumber;

    /** 追溯码 */
    @NotNull
    private String traceNo;

    /** 发票号 */
    private String invoiceNo;

    /** 开始时间 */
    @NotNull
    private Date startTime;

    /** 结束时间 */
    @NotNull
    private Date endTime;

    /** 单价 */
    @NotNull
    private BigDecimal price;

    /** 总价 */
    @NotNull
    private BigDecimal totalPrice;

    /** 售价 */
    @NotNull
    private BigDecimal sellPrice;

    /** 拆零售价 */
    @NotNull
    private BigDecimal minSellPrice;

}
