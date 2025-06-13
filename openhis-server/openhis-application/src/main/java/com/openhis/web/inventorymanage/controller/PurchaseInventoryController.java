/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IPurchaseInventoryAppService;
import com.openhis.web.inventorymanage.dto.InventorySearchParam;
import com.openhis.web.inventorymanage.dto.PurchaseInventoryDto;

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

    @Resource
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
     * 单据号初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/bus-no-init")
    public R<?> purchaseNoInit() {
        return purchaseInventoryAppService.purchaseNoInit();
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
     * 添加/编辑入库单据(批量)
     *
     * @param purchaseInventoryDtoList 入库单据
     * @return 操作结果
     */
    @PutMapping("/inventory-receipt")
    public R<?> addOrEditInventoryReceipt(@Validated @RequestBody List<PurchaseInventoryDto> purchaseInventoryDtoList) {
        return purchaseInventoryAppService.addOrEditInventoryReceipt(purchaseInventoryDtoList);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @DeleteMapping("/inventory-receipt")
    public R<?> deleteInventoryReceipt(@RequestParam List<Long> supplyRequestIds) {
        return purchaseInventoryAppService.deleteReceipt(supplyRequestIds);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/submit-approval")
    public R<?> submitApproval(@RequestBody String busNo) {
        return purchaseInventoryAppService.submitApproval(busNo);
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/withdraw-approval")
    public R<?> withdrawApproval(@RequestBody String busNo) {
        return purchaseInventoryAppService.withdrawApproval(busNo);
    }
}
