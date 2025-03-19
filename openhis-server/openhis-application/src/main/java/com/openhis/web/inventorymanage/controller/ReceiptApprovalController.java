/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IReceiptApprovalAppService;
import com.openhis.web.inventorymanage.dto.ReceiptApprovalSearchParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 单据审批 controller
 *
 * @author zwh
 * @date 2025-03-04
 */
@RestController
@RequestMapping("/inventory-manage/receipt")
@Slf4j
public class ReceiptApprovalController {

    @Autowired
    private IReceiptApprovalAppService receiptApprovalAppService;

    /**
     * 单据审批页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> receiptApprovalInit() {
        return receiptApprovalAppService.receiptApprovalInit();
    }

    /**
     * 审批单据分页列表
     *
     * @param receiptSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 审批单据分页列表
     */
    @GetMapping(value = "/receipt-page")
    public R<?> getPage(ReceiptApprovalSearchParam receiptSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return receiptApprovalAppService.getPage(receiptSearchParam, pageNo, pageSize, searchKey, request);
    }

    /**
     * 入库单据审批通过
     *
     * @param busNo 单据号
     * @param request 请求数据
     * @return 操作结果
     */
    @PostMapping("/purchase-inventory-approved")
    public R<?> purchaseInventoryApproved(@RequestParam String busNo, HttpServletRequest request) {
        return receiptApprovalAppService.purchaseInventoryApproved(busNo, request);
    }

    /**
     * 审批驳回
     *
     * @param busNo 单据号
     * @param request 请求数据
     * @return 操作结果
     */
    @PostMapping("/reject")
    public R<?> reject(@RequestParam String busNo, HttpServletRequest request) {
        return receiptApprovalAppService.reject(busNo, request);
    }
}
