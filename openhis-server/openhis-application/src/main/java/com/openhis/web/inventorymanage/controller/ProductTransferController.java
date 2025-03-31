/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IProductTransferAppService;
import com.openhis.web.inventorymanage.dto.SupplySearchParam;
import com.openhis.web.inventorymanage.dto.ProductTransferDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     * 商品调拨单据详情
     * 
     * @param busNo 单据号
     * @return 供应申请单据详情
     */
    @GetMapping(value = "/product-transfer-detail")
    public R<?> getDetail(@RequestParam String busNo) {
        return productTransferAppService.getDetail(busNo);
    }

    /**
     * 添加/编辑商品调拨单据
     *
     * @param productTransferDto 商品调拨单据
     * @return 操作结果
     */
    @PutMapping("/product-transfer-edit")
    public R<?> addOrEditTransferReceipt(@Validated @RequestBody ProductTransferDto productTransferDto) {
        return productTransferAppService.addOrEditTransferReceipt(productTransferDto);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestId 供应请求id
     * @return 操作结果
     */
    @DeleteMapping("/product-transfer-del")
    public R<?> deleteTransferReceipt(@RequestParam Long supplyRequestId) {
        return productTransferAppService.deleteReceipt(supplyRequestId);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/submit-approval")
    public R<?> submitApproval(@RequestParam String busNo) {
        return productTransferAppService.submitApproval(busNo);
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/withdraw-approval")
    public R<?> withdrawApproval(@RequestParam String busNo) {
        return productTransferAppService.withdrawApproval(busNo);
    }
}
