/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品盘点分页查询条件
 *
 * @author yuxj
 * @date 2025-04-08
 */
@Data
@Accessors(chain = true)
public class ProductStocktakingSearchParam {

    /** 状态 */
    private Integer statusEnum;

    /** 申请人 */
    private Long applicantId;

    /** 单据时间 */
    private Date createTime;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;

    /** 仓库 */
    private String purposeLocationId;

    /** 货位 */
    private String purposeLocationStoreId;
}
