package com.openhis.web.inventorymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存商品明细分页查询条件
 *
 * @author yuanzs
 * @date 2025-04-24
 */
@Data
@Accessors(chain = true)
public class ProductDetailsSearchParam {

    /** 剩余过期天数 */
    private Integer remainingDays;

    /** 项目类型 */
    private Integer categoryCode;

    /** 医保等级 */
    private Integer chrgitmLv;

    /** 仓库药房 */
    private Long locationId;

    /** 库存范围 */
    private Integer warehouseScope;

}
