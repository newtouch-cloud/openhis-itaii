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
import com.openhis.web.reportmanage.appservice.IStocktakingReportAppService;
import com.openhis.web.reportmanage.dto.StocktakingReportSearchParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 库存盘点明细报表 controller
 *
 * @author GYY
 * @date 2025-04-16
 */
@RestController
@RequestMapping("/report-manage/stocktaking")
@Slf4j
public class StocktakingReportController {

    @Autowired
    private IStocktakingReportAppService stocktakingReportAppService;

    /**
     * 库存盘点明细列表
     *
     * @param stocktakingReportSearchParam 库存盘点明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 库存盘点明细
     */
    @GetMapping(value = "/report-stocktaking-page")
    public R<?> getPage(StocktakingReportSearchParam stocktakingReportSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return stocktakingReportAppService.getPage(stocktakingReportSearchParam, pageNo, pageSize, searchKey, request);
    }
}
