/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.controller;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.appservice.IChargeReportAppService;
import com.openhis.web.reportmanage.dto.ChargeReportSearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.openhis.common.enums.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 费用明细报表 controller
 *
 * @author yuxj
 * @date 2025-05-20
 */
@RestController
@RequestMapping("/report-manage/charge")
@Slf4j
public class ChargeReportController {

    @Autowired
    private IChargeReportAppService chargeReportAppService;
    /**
     * 下拉框
     *
     * @return 下拉框信息
     */
    @GetMapping("/init")
    public R<?> chargeReportInit() {
        return chargeReportAppService.chargeReportInit();
    }

    /**
     * 费用明细列表
     *
     * @param chargeReportSearchParam 费用明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 费用明细
     */
    @GetMapping(value = "/report-charge-page")
    public R<?> getPage(ChargeReportSearchParam chargeReportSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "searchKey", required = false) String searchKey,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return chargeReportAppService.getPage(chargeReportSearchParam, pageNo, pageSize, searchKey, request);
    }
}
