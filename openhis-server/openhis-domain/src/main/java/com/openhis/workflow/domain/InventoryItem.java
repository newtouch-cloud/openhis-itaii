package com.openhis.workflow.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.PublicationStatus;

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
    private String categoryCode;

    /** 项目 */
    private String itemTable;

    /** 物品编码 */
    private Long itemId;

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

    /** 最小单位 */
    private String unitCode;

    /** 当前库存数量(最小单位) */
    private BigDecimal quantity;

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

    /** 库位 */
    private Long locationStoreId;

    /** 仓库 */
    private Long locationId;

    /** 追溯码 */
    private String traceNo;

    /** 追溯码包装层级 */
    private Integer packagingLevels;

    /** 采购单价（包装单位） */
    private BigDecimal price;

    public InventoryItem() {
        // 库存状态：启用
        this.inventoryStatusEnum = PublicationStatus.ACTIVE.getValue();
    }

}