/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 供应单据及供应项相关详细信息 dto
 *
 * @author zwh
 * @date 2025-03-06
 */
@Data
@Accessors(chain = true)
public class SupplyItemDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplyId;

    /** 项目 */
    private String itemTable;

    /** 物品编码 */
    private Long itemId;

    /** 数量 */
    private BigDecimal itemQuantity;

    /** 项目单位 */
    private String itemUnit;

    /** 单价 */
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 审批人 */
    private Long approverId;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 目的仓位 */
    private Long purposeLocationStoreId;

    /** 供应商id */
    private Long supplierId;

    /** 产品批号 */
    private String lotNumber;

    /** 追溯码 */
    private String traceNo;

    /** 单据号 */
    private String busNo;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 服务id */
    private Long serviceId;

    /** 名称 */
    private String name;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 常规单位 */
    private String unitCode;

    /** 最小单位 */
    private String minUnitCode;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 费用定价ID */
    private Long definitionId;

    /** 原价 */
    private BigDecimal baseAmount;

    /** 定价子表主键 */
    private Long defDetailId;

    /** 项目类型 */
    private String itemCategory;
}
