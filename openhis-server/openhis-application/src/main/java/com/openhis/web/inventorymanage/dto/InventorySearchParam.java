/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 入库单据分页查询条件
 *
 * @author zwh
 * @date 2025-02-18
 */
@Data
@Accessors(chain = true)
public class InventorySearchParam implements Serializable {

    /** 单据号 */
    @Length(max = 255)
    private String busNo;

    /** 状态 */
    private Integer statusEnum;

    /** 分类 */
    private Integer categoryEnum;

    /** 供应商 */
    private Long supplierId;

    /** 申请开始时间 */
    private Date applyTimeStart;

    /** 申请结束时间 */
    private Date applyTimeEnd;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 申请人 */
    private Long applicantId;
}
