/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

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

    /** 申请时间 */
    private Date applyTime;

    /** 产品批号 */
    @NotNull
    private String lotNumber;

    /** 理由 */
    private String reason;

    /** 理由类型 */
    @NotNull
    private String reasonCode;
}
