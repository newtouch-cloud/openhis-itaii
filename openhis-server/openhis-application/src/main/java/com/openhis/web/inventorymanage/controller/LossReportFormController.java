/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.ILossReportFormAppService;
import com.openhis.web.inventorymanage.dto.LossReportFormDto;
import com.openhis.web.inventorymanage.dto.LossReportSearchParam;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 报损单 controller
 *
 * @author gyy
 * @date 2025-04-03
 */
@RestController
@RequestMapping("/inventory-manage/loss")
@Slf4j
public class LossReportFormController {

    @Autowired
    private ILossReportFormAppService lossReportFormAppService;

    /**
     * 报损单单据列表
     *
     * @param lossReportSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 报损单据分页列表
     */
    @GetMapping(value = "/loss-report-form-page")
    public R<?> getPage(LossReportSearchParam lossReportSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return lossReportFormAppService.getPage(lossReportSearchParam, pageNo, pageSize, searchKey, request);
    }

    /**
     * 报损单初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> lossReportFormInit() {
        return lossReportFormAppService.lossReportFormInit();
    }

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/bus-no-init")
    public R<?> purchaseNoInit() {
        return lossReportFormAppService.lossReportNoInit();
    }

    /**
     * 报损单据详情
     *
     * @param busNo 单据号
     * @return 报损单据详情
     */
    @GetMapping(value = "/loss-receipt")
    public R<?> getDetail(@RequestParam String busNo) {
        return lossReportFormAppService.getDetail(busNo);
    }

    /**
     * 添加/编辑报损单据(批量)
     *
     * @param lossReportFormDtoList 报损单据
     * @return 操作结果
     */
    @PutMapping("/loss-receipt-edit")
    public R<?> addOrEditLossReceipt(@Validated @RequestBody List<LossReportFormDto> lossReportFormDtoList) {
        return lossReportFormAppService.addOrEditLossReceipt(lossReportFormDtoList);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @DeleteMapping("/loss-receipt-del")
    public R<?> deleteInventoryReceipt(@RequestParam List<Long> supplyRequestIds) {
        return lossReportFormAppService.deleteReceipt(supplyRequestIds);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/submit-approval")
    public R<?> submitApproval(@RequestBody String busNo) {
        return lossReportFormAppService.submitApproval(busNo);
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/withdraw-approval")
    public R<?> withdrawApproval(@RequestBody String busNo) {
        return lossReportFormAppService.withdrawApproval(busNo);
    }

}
