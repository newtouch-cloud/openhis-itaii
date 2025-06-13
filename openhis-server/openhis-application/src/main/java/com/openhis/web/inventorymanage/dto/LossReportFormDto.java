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
 * 报损单
 *
 * @author gyy
 * @date 2025-04-07
 */
@Data
@Accessors(chain = true)
public class LossReportFormDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String busNo;

    /** 项目 */
    private String itemTable;

    /** 报损数量 */
    @NotNull
    private BigDecimal itemQuantity;

    /** 物品编码 */
    @NotNull
    private Long itemId;

    /** 物品计量单位 */
    @NotNull
    private String unitCode;

    /** 报损原因 */
    private String lossReason;

    /** 供应商 */
    @NotNull
    private Long supplierId;

    /** 报损仓库类型 */
    @NotNull
    private Integer lossTypeEnum;

    /** 报损仓库 */
    @NotNull
    private Long lossLocationId;

    /** 报损仓位 */
    private Long lossLocationStoreId;

    /** 申请人 */
    @NotNull
    private Long applicantId;

    /** 申请时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 审批人 */
    //@NotNull
    private Long approverId;

    /** 审批日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalTime;

    /** 产品批号 */
    @NotNull
    private String lotNumber;

    /** 开始时间 */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 单价 */
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

}
