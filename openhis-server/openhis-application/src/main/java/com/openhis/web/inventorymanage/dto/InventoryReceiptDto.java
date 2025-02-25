/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 入库单据分页列表 dto
 *
 * @author zwh
 * @date 2025-02-18
 */
@Data
@Accessors(chain = true)
public class InventoryReceiptDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String busNo;

    /** 类型 */
    private Integer typeEnum;

    /** 状态 */
    private Integer statusEnum;

    /** 分类 */
    private Integer categoryEnum;

    /** 供应商 */
    private Long supplierId;

    /** 源仓库 */
    private Long sourceLocationId;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 审批人 */
    private Long approverId;

    /** 审批时间 */
    private Date approvalTime;

    /** 申请人 */
    private Long applicantId;

    /** 申请时间 */
    private Date applyTime;

}
