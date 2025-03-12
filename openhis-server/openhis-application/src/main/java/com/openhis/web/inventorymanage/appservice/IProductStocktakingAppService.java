/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.ProductStocktakingDto;

/**
 * 商品盘点 appService
 *
 * @author zwh
 * @date 2025-03-11
 */
public interface IProductStocktakingAppService {

    /**
     * 添加/编辑商品盘点
     *
     * @param productStocktakingDto 盘点信息
     * @return 操作结果
     */
    R<?> addOrEditProductStocktaking(ProductStocktakingDto productStocktakingDto);
}
