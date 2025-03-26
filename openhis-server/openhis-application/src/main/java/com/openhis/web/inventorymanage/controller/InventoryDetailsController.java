/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IInventoryDetailsAppService;
import com.openhis.web.inventorymanage.dto.PurchaseInSearchParam;
import com.openhis.web.inventorymanage.dto.RequisitionOutSearchParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 库存相关明细查询 controller
 *
 * @author
 * @date 2025-03-10
 */
@RestController
@RequestMapping("/inventory-manage-details")
@Slf4j
public class InventoryDetailsController {

    @Autowired
    private IInventoryDetailsAppService inventoryDetailsAppService;

    /**
     * 采购入库明细查询
     *
     * @param purchaseInSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 采购入库分页列表
     */
    @GetMapping(value = "/purchase-in")
    public R<?> purchaseInQueryGetPage(PurchaseInSearchParam purchaseInSearchParam,
        @RequestParam(name = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return R.ok(
            inventoryDetailsAppService.purchaseInGetPage(purchaseInSearchParam, searchKey, pageNo, pageSize, request));
    }

    /**
     * 领用出库明细查询
     *
     * @param requisitionOutSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 采购入库分页列表
     */
    @GetMapping(value = "/requisition-out")
    public R<?> RequisitionOutQueryGetPage(RequisitionOutSearchParam requisitionOutSearchParam,
        @RequestParam(name = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return R.ok(inventoryDetailsAppService.requisitionOutGetPage(requisitionOutSearchParam, searchKey, pageNo,
            pageSize, request));
    }

    /**
     * 商品调拨明细查询
     *
     * @param requisitionOutSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨分页列表
     */
    @GetMapping(value = "/inventory-transfer")
    public R<?> InventoryTransferQueryGetPage(RequisitionOutSearchParam requisitionOutSearchParam,
        @RequestParam(name = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return R.ok(inventoryDetailsAppService.inventoryTransferGetPage(requisitionOutSearchParam, searchKey, pageNo,
            pageSize, request));
    }

    /**
     * 商品盘点明细查询
     *
     * @param requisitionOutSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品盘点分页列表
     */
    @GetMapping(value = "/inventory-stock-take")
    public R<?> InventoryStockTakeQueryGetPage(RequisitionOutSearchParam requisitionOutSearchParam,
        @RequestParam(name = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return R.ok(inventoryDetailsAppService.inventoryStockTakeGetPage(requisitionOutSearchParam, searchKey, pageNo,
            pageSize, request));
    }

}
