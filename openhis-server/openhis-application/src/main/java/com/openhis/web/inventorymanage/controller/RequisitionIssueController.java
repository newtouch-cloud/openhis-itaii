/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import javax.servlet.http.HttpServletRequest;

import com.openhis.web.inventorymanage.dto.IssueSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IRequisitionIssueAppService;
import com.openhis.web.inventorymanage.dto.IssueDto;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 领用出库 controller
 *
 * @author CY
 * @date 2025-04-03
 */
@RestController
@RequestMapping("/issue-manage/requisition")
@Slf4j
public class RequisitionIssueController {

    @Autowired
    private IRequisitionIssueAppService requisitionIssueAppService;

    /**
     * 领用出库页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> issueInventoryInit() {
        return requisitionIssueAppService.issueInventoryInit();
    }

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/bus-no-init")
    public R<?> requisitionIssueNoInit() {
        return requisitionIssueAppService.requisitionIssueNoInit();
    }

    /**
     * 领用出库单据列表
     *
     * @param issueSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 领用出库单据分页列表
     */
    @GetMapping(value = "/requisition-issue-page")
    public R<?> getPage(IssueSearchParam issueSearchParam,
                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return requisitionIssueAppService.getPage(issueSearchParam, pageNo, pageSize, searchKey, request);
    }

    /**
     * 领用出库单据详情
     *
     * @param busNo 单据号
     * @return 入库单据详情
     */
    @GetMapping(value = "/requisition-issue-detail")
    public R<?> getDetail(@RequestParam String busNo) {
        return requisitionIssueAppService.getDetail(busNo);
    }

    /**
     * 添加/编辑领用单据(批量)
     *
     * @param requisitionIssueDtoList 领用单据
     * @return 操作结果
     */
    @PutMapping("/requisition-issue-edit")
    public R<?> addOrEditInventoryReceipt(@Validated @RequestBody List<IssueDto> requisitionIssueDtoList) {
        return requisitionIssueAppService.addOrEditIssueReceipt(requisitionIssueDtoList);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @DeleteMapping("/requisition-issue-del")
    public R<?> deleteInventoryReceipt(@RequestParam List<Long> supplyRequestIds) {
        return requisitionIssueAppService.deleteReceipt(supplyRequestIds);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/submit-approval")
    public R<?> submitApproval(@RequestBody String busNo) {
        return requisitionIssueAppService.submitApproval(busNo);
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/withdraw-approval")
    public R<?> withdrawApproval(@RequestBody String busNo) {
        return requisitionIssueAppService.withdrawApproval(busNo);
    }
}
