/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 项目价格详情
 *
 * @author zwh
 * @date 2025-03-07
 */
@Data
@Accessors(chain = true)
public class ItemChargeDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 费用定价主键ID */
    private Long definitionId;

    /** 名称 */
    private String chargeName;

    /** 批次号 */
    private String conditionLotnumber;

    /** 条件类型 */
    private String conditionUnitCode;

    /** 单位价格 */
    private BigDecimal unitPrice;

    /** 关联项目 */
    private Long instanceId;

    /** 基础价格 */
    private BigDecimal sellPrice;
}
