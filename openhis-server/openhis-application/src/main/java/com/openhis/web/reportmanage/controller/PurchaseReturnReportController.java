/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.controller;

import javax.servlet.http.HttpServletRequest;

import com.openhis.web.reportmanage.appservice.PurchaseReturnReportAppService;
import com.openhis.web.reportmanage.dto.PurchaseReturnReportSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;

import lombok.extern.slf4j.Slf4j;

/**
 * 采购退货明细报表 controller
 *
 * @author ym
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/report-manage/purchase-return")
@Slf4j
public class PurchaseReturnReportController {

    @Autowired
    private PurchaseReturnReportAppService purchaseReturnReportAppService;

    /**
     * 采购退货明细页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        return purchaseReturnReportAppService.init();
    }

    /**
     * 采购退货明细列表
     *
     * @param purchaseReturnReportSearchParam 采购退货明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 采购退货明细
     */
    @GetMapping(value = "/report-purchase-return")
    public R<?> getPage(PurchaseReturnReportSearchParam purchaseReturnReportSearchParam,
                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return purchaseReturnReportAppService.getPage(purchaseReturnReportSearchParam, pageNo, pageSize, searchKey, request);
    }
}
