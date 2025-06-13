/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存商品明细报表 dto
 *
 * @author GYY
 * @date 2025-04-21
 */
@Data
@Accessors(chain = true)
public class InventoryProductReportPageDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编码 */
    private String busNo;

    /** 项目名称 */
    private String name;

    /** 产品批号 */
    private String lotNumber;

    /** 厂家 */
    private String manufacturerText;

    /** 项目(药品类型) */
    private Integer itemTable;
    private String itemTableText;

    /** 库存数量 */
    private BigDecimal itemQuantity;

    /** 计量单位 */
    private String unitCode;

    /** 小包装库存数 */
    private BigDecimal minPackageQuantity;

    /** 小包装单位 */
    private String minPackageUnit;

    /** 进价(采购单价) */
    private BigDecimal price;

    /** 售价 */
    private BigDecimal salePrice;

    /** 拆零进价 */
    private BigDecimal partPrice;

    /** 拆零售价 */
    private BigDecimal partSalePrice;

    /** 进价金额(总计) */
    private BigDecimal totalPrice;

    /** 售价金额(总计) */
    private BigDecimal totalSalePrice;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;
    private String purposeTypeEnum_enumText;

    /** 仓库 */
    private String locationName;

    /** 仓库货位 */
    private String locationStoreName;

    /** 有效期 */
    private Date expirationDate;

    /** 医保编码 */
    private String ybNo;
}
