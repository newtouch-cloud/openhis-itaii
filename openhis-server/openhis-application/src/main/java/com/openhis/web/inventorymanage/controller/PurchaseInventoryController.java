/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IPurchaseInventoryAppService;
import com.openhis.web.inventorymanage.dto.InventoryReceiptDto;
import com.openhis.web.inventorymanage.dto.InventorySearchParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 采购入库 controller
 *
 * @author zwh
 * @date 2025-02-18
 */
@RestController
@RequestMapping("/inventory-manage/purchase")
@Slf4j
public class PurchaseInventoryController {

    @Autowired
    private IPurchaseInventoryAppService purchaseInventoryAppService;

    /**
     * 入库单据页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> purchaseInventoryInit() {
        return purchaseInventoryAppService.purchaseInventoryInit();
    }

    /**
     * 入库单据列表
     *
     * @param inventorySearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 入库单据分页列表
     */
    @GetMapping(value = "/inventory-receipt-page")
    public R<?> getPage(InventorySearchParam inventorySearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return purchaseInventoryAppService.getPage(inventorySearchParam, pageNo, pageSize, searchKey, request);
    }

    /**
     * 入库单据详情
     * 
     * @param busNo 单据号
     * @return 入库单据详情
     */
    @GetMapping(value = "/inventory-receipt")
    public R<?> getDetail(@RequestParam String busNo) {
        return purchaseInventoryAppService.getDetail(busNo);
    }

    /**
     * 添加/编辑入库单据
     *
     * @param inventoryReceiptDto 入库单据
     * @return 操作结果
     */
    @PutMapping("/inventory-receipt")
    public R<?> addOrEditInventoryReceipt(@Validated @RequestBody InventoryReceiptDto inventoryReceiptDto) {
        return purchaseInventoryAppService.addOrEditInventoryReceipt(inventoryReceiptDto);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestId 供应请求id
     * @return 操作结果
     */
    @DeleteMapping("/inventory-receipt")
    public R<?> deleteInventoryReceipt(@RequestParam Long supplyRequestId) {
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
