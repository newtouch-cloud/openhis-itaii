/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IReturnIssueAppService;
import com.openhis.web.inventorymanage.dto.IssueDto;
import com.openhis.web.inventorymanage.dto.IssueSearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 退货出库 controller
 *
 * @author CY
 * @date 2025-04-08
 */
@RestController
@RequestMapping("/issue-manage/return")
@Slf4j
public class ReturnIssueController {

    @Autowired
    private IReturnIssueAppService returnIssueAppService;

    /**
     * 退货出库页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> returnIssueInit() {
        return returnIssueAppService.returnIssueInit();
    }

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/bus-no-init")
    public R<?> returnIssueNoInit() {
        return returnIssueAppService.returnIssueNoInit();
    }

    /**
     * 退货出库单据列表
     *
     * @param issueSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 退货出库单据分页列表
     */
    @GetMapping(value = "/return-issue-page")
    public R<?> getPage(IssueSearchParam issueSearchParam,
                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return returnIssueAppService.getPage(issueSearchParam, pageNo, pageSize, searchKey, request);
    }

    /**
     * 退货出库单据详情
     *
     * @param busNo 单据号
     * @return 退货出库单据详情
     */
    @GetMapping(value = "/return-issue-detail")
    public R<?> getDetail(@RequestParam String busNo) {
        return returnIssueAppService.getDetail(busNo);
    }

    /**
     * 添加/编辑退货出库单据(批量)
     *
     * @param returnIssueDtoList  退货出库单据
     * @return 操作结果
     */
    @PutMapping("/return-issue-edit")
    public R<?> addOrEditInventoryReceipt(@Validated @RequestBody List<IssueDto> returnIssueDtoList ) {
        return returnIssueAppService.addOrEditIssueReceipt(returnIssueDtoList);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @DeleteMapping("/return-issue-del")
    public R<?> deleteInventoryReceipt(@RequestParam List<Long> supplyRequestIds) {
        return returnIssueAppService.deleteReceipt(supplyRequestIds);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/submit-approval")
    public R<?> submitApproval(@RequestBody String busNo) {
        return returnIssueAppService.submitApproval(busNo);
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/withdraw-approval")
    public R<?> withdrawApproval(@RequestBody String busNo) {
        return returnIssueAppService.withdrawApproval(busNo);
    }
}
