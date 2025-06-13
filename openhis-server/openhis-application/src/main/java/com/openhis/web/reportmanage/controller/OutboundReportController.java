package com.openhis.web.reportmanage.controller;

import javax.servlet.http.HttpServletRequest;

import com.openhis.web.reportmanage.appservice.IOutboundReportAppService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.dto.OutboundReportSearchParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 出库明细报表 controller
 *
 * @author yuanzs
 * @date 2025-04-21
 */
@RestController
@RequestMapping("/report-manage/outbound")
@Slf4j
@AllArgsConstructor
public class OutboundReportController {

    @Autowired
    private IOutboundReportAppService outboundReportAppService;

    /**
     * 出库明细页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        return outboundReportAppService.init();
    }

    /**
     * 出库明细
     *
     * @param outboundReportSearchParam 出库明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 出库明细分页列表
     */
    @GetMapping(value = "/report-outbound-page")
    public R<?> getPage(OutboundReportSearchParam outboundReportSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return outboundReportAppService.getPage(outboundReportSearchParam, pageNo, pageSize, searchKey, request);
    }

}
