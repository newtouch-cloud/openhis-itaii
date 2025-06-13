/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import javax.servlet.http.HttpServletRequest;

import com.openhis.web.inventorymanage.dto.ReceiptDetailDto;
import com.openhis.web.inventorymanage.dto.StocktakingBatchSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IProductStocktakingAppService;
import com.openhis.web.inventorymanage.dto.ProductStocktakingDto;
import com.openhis.web.inventorymanage.dto.ProductStocktakingSearchParam;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

    /**
     * 商品盘点页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> productStocktakingInit() {
        return productStocktakingAppService.productStocktakingInit();
    }

    /**
     * 获取单据号
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/detail-init")
    public R<?> productStocktakingBusNoInit() {
        return productStocktakingAppService.productStocktakingBusNoInit();
    }

    /**
     * 商品盘点列表
     *
     * @param stocktakingSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 入库单据分页列表
     */
    @GetMapping(value = "/stocktaking-receipt-page")
    public R<?> getPage(ProductStocktakingSearchParam stocktakingSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return productStocktakingAppService.getPage(stocktakingSearchParam, pageNo, pageSize, searchKey, request);
    }

    /**
     * 盘点单据详情
     *
     * @param busNo 单据号
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 入库单据详情
     */
    @GetMapping(value = "/stocktaking-receipt")
    public R<?> getDetail(@RequestParam String busNo,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return productStocktakingAppService.getDetail(busNo, pageNo, pageSize);
    }

    /**
     * 添加/编辑商品盘点(批量)
     *
     * @param productStocktakingDtoList 盘点信息
     * @return 操作结果
     */
    @PutMapping("/product-stocktaking")
    public R<?> addOrEditProductStocktaking(@Validated @RequestBody List<ProductStocktakingDto> productStocktakingDtoList) {
        return productStocktakingAppService.addOrEditProductStocktaking(productStocktakingDtoList);
    }

    /**
     * 生成批量盘点单
     *
     * @param stocktakingSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 盘点单据分页列表
     */
    @GetMapping(value = "/stocktaking-receipt-batch")
    public R<?> getBatchDetail(StocktakingBatchSearchParam stocktakingSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return productStocktakingAppService.getBatchDetail(stocktakingSearchParam, pageNo, pageSize, searchKey,
            request);
    }

    /**
     * 保存批量盘点单
     *
     * @param productStocktakingDtoList 盘点单据
     * @return 执行结果
     */
    @PutMapping(value = "/stocktaking-receipt-addBatch")
    public R<?> addBatchDetail(@RequestBody List<ProductStocktakingDto> productStocktakingDtoList) {
        return productStocktakingAppService.addBatchDetail(productStocktakingDtoList);
    }

    /**
     * 删除盘点单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @DeleteMapping("/product-stocktaking")
    public R<?> deleteProductStocktaking(@RequestParam List<Long> supplyRequestIds) {
        return productStocktakingAppService.deleteReceipt(supplyRequestIds);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/submit-approval")
    public R<?> submitApproval(@RequestBody String busNo) {
        return productStocktakingAppService.submitApproval(busNo);
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/withdraw-approval")
    public R<?> withdrawApproval(@RequestBody String busNo) {
        return productStocktakingAppService.withdrawApproval(busNo);
    }

}
