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
import com.openhis.web.reportmanage.appservice.IInboundReportAppService;
import com.openhis.web.reportmanage.dto.InboundReportSearchParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 入库明细报表 controller
 *
 * @author GYY
 * @date 2025-04-22
 */
@RestController
@RequestMapping("/report-manage/inbound")
@Slf4j
public class InboundReportController {

    @Autowired
    private IInboundReportAppService inboundReportAppService;

    /**
     * 入库明细页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        return inboundReportAppService.init();
    }

    /**
     * 入库明细列表
     *
     * @param inboundReportSearchParam 入库明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 入库明细
     */
    @GetMapping(value = "/report-inbound-page")
    public R<?> getPage(InboundReportSearchParam inboundReportSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return inboundReportAppService.getPage(inboundReportSearchParam, pageNo, pageSize, searchKey, request);
    }
}
