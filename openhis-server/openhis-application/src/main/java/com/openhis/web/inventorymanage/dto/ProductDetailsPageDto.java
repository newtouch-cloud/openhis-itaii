package com.openhis.web.inventorymanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存商品明细分页列表 dto
 *
 * @author yuanzs
 * @date 2025-04-24
 */
@Data
@Accessors(chain = true)
public class ProductDetailsPageDto {

    /** 库存项目管理ID（停供用） */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 药品编码 */
    private String busNo;

    /** 药品名称 */
    private String medicineName;

    /** 规格 */
    private String totalVolume;

    /** 医保等级 */
    private Integer chrgitmLv;
    private String chrgitmLv_enumText;

    /** 厂家 */
    private String manufacturerText;

    /** 生产批号 */
    private String lotNumber;

    /** 药品类型 */
    private String categoryCode;

    /** 药品单位（采购单位） */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 库存数量 */
    private BigDecimal quantity;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 库存单位 */
    @Dict(dictCode = "unit_code")
    private String minUnitCode;
    private String minUnitCode_dictText;

    /** 采购价格 */
    private BigDecimal price;

    /** 生产日期 */
    private Date productionDate;

    /** 失效日期 */
    private Date expirationDate;

    /** 剩余过期天数 */
    private Integer remainingDays;

    /** 药品追溯码 */
    private String traceNo;

    /** 药品停用 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 仓库 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;
    private String locationName;

    /** 货位 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationStoreId;
    private String locationStoreName;

    /** 仓库类型 */
    private Integer formEnum;
    private String formEnum_enumText;

    /** 供应商 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_supplier", dictCode = "id", dictText = "name")
    private Long supplyId;
    private String supplyId_dictText;

    /** 停供状态 */
    private Integer inventoryStatusEnum;
    private String inventoryStatusEnum_enumText;

}
