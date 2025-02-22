package com.openhis.workflow.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 库存项目管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("wor_inventory_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InventoryItem extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 物品类别 */
    private Integer categoryEnum;

    /** 编码 */
    private String busNo;

    /** 名称 */
    private String name;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 库存状态 */
    private Integer inventoryStatusEnum;

    /** 供应商id */
    private Long supplierId;

    /** 说明书 */
    private String descriptionText;

    /** 常规单位 */
    private String baseUnitCode;

    /** 当前库存数量(常规单位) */
    private BigDecimal baseQuantity;

    /** 最小单位 */
    private String minUnitCode;

    /** 当前库存数量(最小单位数量) */
    private BigDecimal minQuantity;

    /** 最小库存警戒数量(常规单位) */
    private BigDecimal itemMinQuantity;

    /** 最大库存警戒数量(常规单位) */
    private BigDecimal itemMaxQuantity;

    /** 特征 */
    private String characteristicJson;

    /** 产品批号 */
    private String lotNumber;

    /** 生产日期 */
    private Date productionDate;

    /** 失效日期 */
    private Date expirationDate;

    /** 有效期(月) */
    private Integer validityMon;

    /** 仓库 */
    private Long locationStoreId;

    /** 库位 */
    private Long locationId;

    /** 追溯码 */
    private String traceNo;

    /** 追溯码包装层级 */
    private Integer packagingLevels;


}