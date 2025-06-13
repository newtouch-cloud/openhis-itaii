/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.common.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存项目所在对应库房信息
 *
 * @author zwh
 * @date 2025-04-01
 */
@Data
@Accessors(chain = true)
public class LocationInventoryDto {

    /** 物理表名 */
    private String itemTable;

    /** 实例id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    /** 采购单价 */
    private BigDecimal price;

    /** 产品批号 */
    private String lotNumber;

    /** 生产日期 */
    private Date productionDate;

    /** 失效日期 */
    private Date expirationDate;

    /** 仓库类型 */
    private String formEnum;

    /** 库位 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationStoreId;

    /** 仓库 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /** 库房名称 */
    private String locationName;

    /** 货位名称 */
    private String locationStoreName;

    /** 原仓库数量(最小单位) */
    private BigDecimal orgQuantity;

    /** 目的仓库数量(最小单位) */
    private BigDecimal objQuantity;

    /** 供应商id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 医保编码 */
    private String ybNo;

    /** 生产厂家 */
    private String manufacturer;

}
