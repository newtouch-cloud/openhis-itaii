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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 出库单据
 *
 * @author CY
 * @date 2025-04-07
 */
@Data
@Accessors(chain = true)
public class IssueDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String busNo;

    /** 项目 */
    private String itemTable;

    /** 数量 */
    @NotNull
    @Min(1)
    private BigDecimal itemQuantity;

    /** 物品编码 */
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    /** 物品计量单位 */
    @NotNull
    private String unitCode;

    /** 请求细节 */
    private String detailJson;

    /** 供应商 */
    private Long supplierId;

    /** 源仓库类型 */
    @NotNull
    private Integer sourceTypeEnum;

    /** 源仓库 */
    @NotNull
    private Long sourceLocationId;

    /** 源仓位 */
    private Long sourceLocationStoreId;

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

    /** 经手人 */
    private Long practitionerId;

    /** 申请时间 */
    private Date applyTime;

    /** 产品批号 */
    @NotNull
    private String lotNumber;

    /** 追溯码 */
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
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 备注 */
    private String remake;
}
