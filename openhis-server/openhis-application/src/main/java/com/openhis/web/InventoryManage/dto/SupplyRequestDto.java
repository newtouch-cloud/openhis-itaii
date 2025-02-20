/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ZhangYC
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class SupplyRequestDto {

    /** 项目 */
    private String itemTable;

    /** 数量 */
    private Integer itemQuantity;

    /** 物品编码 */
    private Long code;

    /** 物品计量单位 */
    private String unitIdCode;

    /** 物品数量 */
    private Integer unitQuantity;

    /** 请求细节 */
    private String detailJson;

    /** 期望时间 */
    private Date occurrenceTime;

    /** 供应人 */
    private Long practitionerId;

    /** 供应商 */
    private Long supplierId;

    /** 单位 */
    private Long quantityUnit;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

}
