/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IReceiptApprovalAppService;

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
