/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.appservice.ITransferReportAppService;
import com.openhis.web.reportmanage.dto.TransferReportSearchParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品调拨明细报表 controller
 *
 * @author GYY
 * @date 2025-04-15
 */
@RestController
@RequestMapping("/report-manage/transfer")
@Slf4j
public class TransferReportController {

    @Autowired
    private ITransferReportAppService transferReportAppService;

    /**
     * 商品调拨明细列表
     *
     * @param transferReportSearchParam 商品调拨明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨明细
     */
    @GetMapping(value = "/report-transfer-page")
    public R<?> getPage(TransferReportSearchParam transferReportSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "searchKey", required = false) String searchKey,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return transferReportAppService.getPage(transferReportSearchParam, pageNo, pageSize, searchKey, request);
    }
}
