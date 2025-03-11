/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

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

    /** 条件 */
    private String conditionCode;

    /** 命中值 */
    private String conditionValue;

    /** 单位价格 */
    private BigDecimal unitPrice;

    /** 关联项目 */
    private Long instanceId;

    /** 基础价格 */
    private BigDecimal sellPrice;

    /** 药品单位 */
    private String unitCode;

    /** 最小单位 */
    private String minUnitCode;
}
