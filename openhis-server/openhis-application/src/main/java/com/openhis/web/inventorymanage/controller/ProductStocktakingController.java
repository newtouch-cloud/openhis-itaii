/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IProductStocktakingAppService;
import com.openhis.web.inventorymanage.appservice.IPurchaseInventoryAppService;
import com.openhis.web.inventorymanage.dto.ProductStocktakingDto;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品盘点 controller
 *
 * @author zwh
 * @date 2025-03-11
 */
@RestController
@RequestMapping("/inventory-manage/stocktaking")
@Slf4j
public class ProductStocktakingController {

    @Autowired
    private IProductStocktakingAppService productStocktakingAppService;

    @Autowired
    private IPurchaseInventoryAppService purchaseInventoryAppService;

    /**
     * 添加/编辑商品盘点
     *
     * @param productStocktakingDto 盘点信息
     * @return 操作结果
     */
    @PutMapping("/product-stocktaking")
    public R<?> addOrEditProductStocktaking(@Validated @RequestBody ProductStocktakingDto productStocktakingDto) {
        return productStocktakingAppService.addOrEditProductStocktaking(productStocktakingDto);
    }

    /**
     * 删除盘点单据
     *
     * @param supplyRequestId 供应请求id
     * @return 操作结果
     */
    @DeleteMapping("/product-stocktaking")
    public R<?> deleteProductStocktaking(@RequestParam Long supplyRequestId) {
        return purchaseInventoryAppService.deleteReceipt(supplyRequestId);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/submit-approval")
    public R<?> submitApproval(@RequestParam String busNo) {
        return purchaseInventoryAppService.submitApproval(busNo);
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/withdraw-approval")
    public R<?> withdrawApproval(@RequestParam String busNo) {
        return purchaseInventoryAppService.withdrawApproval(busNo);
    }

}
