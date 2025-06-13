/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应单据及供应项相关详细信息 dto
 *
 * @author zwh
 * @date 2025-03-06
 */
@Data
@Accessors(chain = true)
public class SupplyItemDetail3503Dto {

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

    /** 源仓库 */
    private Long sourceLocationId;

    /** 源仓位 */
    private Long sourceLocationStoreId;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 目的仓位 */
    private Long purposeLocationStoreId;

    /** 供应商id */
    private Long supplierId;

    /** 供应商 */
    private String supplierName;

    /** 经手人 */
    private String practitionerName;

    /** 产品批号 */
    private String lotNumber;

    /** 追溯码 */
    private String traceNo;

    /** 发票码 */
    private String invoiceNo;

    /** 项目医保码 */
    private String ybNo;

    /** 单据号 */
    private String busNo;

    /** 项目编号 */
    private String itemBusNo;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 服务id */
    private Long serviceId;

    /** 项目名称 */
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

    /** 项目类型 */
    private String itemCategory;

    /** 处方标志 */
    private Integer rxFlag;

    /** 批准文号 */
    private String approvalNumber;

    /** 生产厂商文本 */
    private String manufacturerText;
}
