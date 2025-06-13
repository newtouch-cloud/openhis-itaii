/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.controller;

import javax.servlet.http.HttpServletRequest;

import com.openhis.web.reportmanage.appservice.ILossReportAppService;
import com.openhis.web.reportmanage.dto.LossReportSearchParam;
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
 * 报损明细报表 controller
 *
 * @author ym
 *
 * @date 2025-05-21
 */
@RestController
@RequestMapping("/report-manage/loss")
@Slf4j
public class LossReportController {

    @Autowired
    private ILossReportAppService lossReportAppService;

    /**
     * 报损明细列表
     *
     * @param lossReportSearchParam 报损明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 报损明细
     */
    @GetMapping(value = "/report-loss-page")
    public R<?> getPage(LossReportSearchParam lossReportSearchParam,
                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return lossReportAppService.getPage(lossReportSearchParam, pageNo, pageSize, searchKey, request);
    }
}
