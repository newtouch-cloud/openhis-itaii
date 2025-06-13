/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IPurchaseReturnAppService;
import com.openhis.web.inventorymanage.dto.PurchaseReturnDetailDto;
import com.openhis.web.inventorymanage.dto.PurchaseReturnSearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 采购退货 controller
 *
 * @author yuanzs
 * @date 2025-04-02
 */
@RestController
@RequestMapping("/inventory-manage/return")
@Slf4j
public class PurchaseReturnController {

    @Autowired
    private IPurchaseReturnAppService purchaseReturnAppService;

    /**
     * 采购退货页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> purchaseReturnInit() {
        return purchaseReturnAppService.purchaseReturnInit();
    }

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/bus-no-init")
    public R<?> purchaseNoInit() {
        return purchaseReturnAppService.purchaseNoInit();
    }

    /**
     * 根据单据号查询详情
     *
     * @param busNo 单据号
     * @return 退货单
     */
    @GetMapping(value = "/return-detail")
    public R<?> getPurchaseReturnOrder(@RequestParam String busNo) {
        return purchaseReturnAppService.getDetail(busNo);
    }

    /**
     * 根据入库单据号查询已生成的退货单
     *
     * @param busNo 单据号
     * @return 退货单
     */
    @GetMapping(value = "/generated-return-detail")
    public R<?> getGeneratedReturnOrder(@RequestParam String busNo) {
        return purchaseReturnAppService.getGeneratedPage(busNo);
    }

    /**
     * 生成退货单(批量)
     *
     * @param purchaseReturnDetailDtoList 退货单据号列表
     * @return 操作结果
     */
    @PutMapping(value = "/generate-return-receipt")
    public R<?> generateReturnReceipt(@Validated @RequestBody List<PurchaseReturnDetailDto> purchaseReturnDetailDtoList) {
        return purchaseReturnAppService.returnGenerateBusNo(purchaseReturnDetailDtoList);
    }

    /**
     * 删除退货单
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @DeleteMapping(value = "/delete-return-receipt")
    public R<?> deleteReturnReceipt(@RequestParam List<Long> supplyRequestIds) {
        return purchaseReturnAppService.returnDeleteBusNo(supplyRequestIds);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/return-submit-approval")
    public R<?> submitApproval(@RequestBody String busNo) {
        return purchaseReturnAppService.submitApproval(busNo);
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/return-withdraw-approval")
    public R<?> withdrawApproval(@RequestBody String busNo) {
        return purchaseReturnAppService.withdrawApproval(busNo);
    }

}
