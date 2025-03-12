/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import com.core.common.utils.DateUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.common.enums.SupplyCategory;
import com.openhis.common.enums.SupplyType;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.ISupplyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IProductStocktakingAppService;
import com.openhis.web.inventorymanage.dto.ProductStocktakingDto;

/**
 * 商品盘点 impl
 *
 * @author zwh
 * @date 2025-03-11
 */
@Service
public class ProductStocktakingAppServiceImpl implements IProductStocktakingAppService {

    @Autowired
    private ISupplyRequestService supplyRequestService;

    /**
     * 添加/编辑商品盘点
     *
     * @param productStocktakingDto 盘点信息
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditProductStocktaking(ProductStocktakingDto productStocktakingDto) {
        // 初始化盘点信息
        SupplyRequest supplyRequest = new SupplyRequest();
        BeanUtils.copyProperties(productStocktakingDto, supplyRequest);

        if (productStocktakingDto.getId() != null) {
            // 更新盘点信息
            supplyRequestService.updateById(supplyRequest);
        } else {
            // 生成待发送的盘点单据
            supplyRequest
                    // 单据分类：非库存供应
                    .setCategoryEnum(SupplyCategory.STOCK_SUPPLY.getValue())
                    // 单据类型：商品盘点
                    .setTypeEnum(SupplyType.PRODUCT_INVENTORY.getValue())
                    // 申请时间
                    .setApplyTime(DateUtils.getNowDate());
            supplyRequestService.save(supplyRequest);
        }
        // 返回单据id
        return R.ok(supplyRequest.getId(), null);
    }
}
