/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品盘点 dto
 *
 * @author zwh
 * @date 2025-03-11
 */
@Data
@Accessors(chain = true)
public class ProductStocktakingDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String busNo;

    /** 项目 */
    private String itemTable;

    /** 项目id */
    @NotNull
    private Long itemId;

    /** 物品计量单位 */
    private String unitCode;

    /** 数量 */
    @NotNull
    private BigDecimal itemQuantity;

    /** 单价 */
    @NotNull
    private BigDecimal price;

    /** 总金额 */
    @NotNull
    private BigDecimal totalPrice;

    /** 制单日期（盘点日期） */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date occurrenceTime;

    /** 理由 */
    private String reason;

    /** 理由类型 */
    private String reasonCode;

    /** 目的仓库类型 */
    @NotNull
    private Integer purposeTypeEnum;

    /** 目的仓库 */
    @NotNull
    private Long purposeLocationId;

    /** 目的仓位 */
    private Long purposeLocationStoreId;

    /** 申请人 */
    private Long applicantId;

    /** 供应商 */
    private Long supplierId;

    /** 开始时间 */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 产品批号 */
    @NotNull
    private String lotNumber;

    /** 追溯码 */
    private String traceNo;
}
