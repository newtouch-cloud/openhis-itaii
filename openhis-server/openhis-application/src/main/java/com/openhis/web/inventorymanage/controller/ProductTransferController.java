/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import javax.servlet.http.HttpServletRequest;

import com.openhis.web.inventorymanage.dto.BatchTransferSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IProductTransferAppService;
import com.openhis.web.inventorymanage.dto.ProductTransferDto;
import com.openhis.web.inventorymanage.dto.SupplySearchParam;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 商品调拨 controller
 *
 * @author MY
 * @date 2025-03-19
 */
@RestController
@RequestMapping("/inventory-manage/transfer")
@Slf4j
public class ProductTransferController {

    @Autowired
    private IProductTransferAppService productTransferAppService;

    /**
     * 商品调拨页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> productTransferInit() {
        return productTransferAppService.productTransferInit();
    }

    /**
     * 商品调拨单据编号初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/bus-no-init")
    public R<?> productTransferNoInit() {
        return productTransferAppService.productTransferNoInit();
    }

    /**
     * 商品调拨单据列表
     *
     * @param supplySearchParam 供应申请查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨分页列表
     */
    @GetMapping(value = "/product-transfer-page")
    public R<?> getPage(SupplySearchParam supplySearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return productTransferAppService.getPage(supplySearchParam, pageNo, pageSize, searchKey, request);
    }

    /**
     * 生成商品批量调拨单据
     *
     * @param batchTransferSearchParam 生成批量调拨单查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 批量调拨单据
     */
    @GetMapping(value = "/product-transfer-batch")
    public R<?> createBatchTransfer(BatchTransferSearchParam batchTransferSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return productTransferAppService.createBatchTransfer(batchTransferSearchParam, pageNo, pageSize, request);
    }

    /**
     * 保存商品批量调拨单据
     *
     * @param productTransferDtoList 商品批量调拨单据
     * @return 操作结果
     */
    @PutMapping("/product-transfer-batch")
    public R<?> addOrEditBatchTransferReceipt(@RequestBody List<ProductTransferDto> productTransferDtoList) {
        // 批量保存按钮
        Boolean flag = true;
        return productTransferAppService.addOrEditBatchTransferReceipt(productTransferDtoList, flag);
    }

    /**
     * 保存商品批量调拨单据(单条保存)
     *
     * @param productTransferDtoList 商品批量调拨单据
     * @return 操作结果
     */
    @PutMapping("/transfer-batch")
    public R<?> addOrEditBatchTransfer(@RequestBody List<ProductTransferDto> productTransferDtoList) {
        // 单独保存
        Boolean flag = false;
        return productTransferAppService.addOrEditBatchTransferReceipt(productTransferDtoList, flag);
    }

    /**
     * 商品调拨单据详情
     *
     * @param busNo 单据号
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 供应申请单据详情
     */
    @GetMapping(value = "/product-transfer-detail")
    public R<?> getDetail(@RequestParam String busNo,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return productTransferAppService.getDetail(busNo, pageNo, pageSize);
    }

    /**
     * 添加/编辑商品调拨单据(批量)
     *
     * @param productTransferDtoList 商品调拨单据
     * @return 操作结果
     */
    @PutMapping("/product-transfer")
    public R<?> addOrEditTransferReceipt(@Validated @RequestBody List<ProductTransferDto> productTransferDtoList) {
        return productTransferAppService.addOrEditTransferReceipt(productTransferDtoList);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @DeleteMapping("/product-transfer-del")
    public R<?> deleteTransferReceipt(@RequestParam List<Long> supplyRequestIds) {
        return productTransferAppService.deleteReceipt(supplyRequestIds);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/submit-approval")
    public R<?> submitApproval(@RequestBody String busNo) {
        return productTransferAppService.submitApproval(busNo);
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/withdraw-approval")
    public R<?> withdrawApproval(@RequestBody String busNo) {
        return productTransferAppService.withdrawApproval(busNo);
    }
}
