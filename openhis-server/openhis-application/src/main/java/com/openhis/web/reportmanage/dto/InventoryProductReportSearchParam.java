/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存商品明细查询条件
 *
 * @author GYY
 * @date 2025-04-21
 */
@Data
@Accessors(chain = true)
public class InventoryProductReportSearchParam {

    /** 编码 */
    private String busNo;

    /** 项目名称 */
    private String name;

    /** 项目类型 */
    private Integer categoryType;

    /** 制单时间 */
    private Date occurrenceTime;

    /** 药房类型 */
    private Integer purposeTypeEnum;

    /** 库存范围 */
    private Integer inventoryScope;

    /** 供应商 */
    private Long supplierId;
}
