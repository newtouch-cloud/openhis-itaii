package com.openhis.web.reportmanage.controller;

import javax.servlet.http.HttpServletRequest;

import com.openhis.web.reportmanage.appservice.IReturnIssueReportAppService;
import com.openhis.web.reportmanage.dto.ReturnIssueReportSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 领用退库明细报表 controller
 *
 * @author ym
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/report-manage/return-issue")
@Slf4j
@AllArgsConstructor
public class ReturnIssueReportController {

    @Autowired
    private IReturnIssueReportAppService returnIssueReportAppService;

    /**
     * 领用退库明细页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        return returnIssueReportAppService.init();
    }

    /**
     * 领用退库明细
     *
     * @param returnIssueReportSearchParam 领用退库明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 领用退库明细分页列表
     */
    @GetMapping(value = "/report-return-issue")
    public R<?> getPage(ReturnIssueReportSearchParam returnIssueReportSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return returnIssueReportAppService.getPage(returnIssueReportSearchParam, pageNo, pageSize, searchKey, request);
    }

}
