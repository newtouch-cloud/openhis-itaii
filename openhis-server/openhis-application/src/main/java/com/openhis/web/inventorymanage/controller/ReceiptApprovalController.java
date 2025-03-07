/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.framework.web.service.TokenService;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.domain.ChargeItemDefApp;
import com.openhis.administration.service.IChargeItemDefAppService;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.inventorymanage.appservice.IInventoryManageService;
import com.openhis.web.inventorymanage.assembler.InventoryManageAssembler;
import com.openhis.web.inventorymanage.dto.ItemChargeDetailDto;
import com.openhis.web.inventorymanage.dto.SupplyItemDetailDto;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.domain.SupplyDelivery;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.IInventoryItemService;
import com.openhis.workflow.service.ISupplyDeliveryService;
import com.openhis.workflow.service.ISupplyRequestService;

import lombok.extern.slf4j.Slf4j;

/**
 * 单据审批 controller
 *
 * @author zwh
 * @date 2025-03-04
 */
@RestController
@RequestMapping("/inventory-manage/receipt")
@Slf4j
public class ReceiptApprovalController {

    @Autowired
    private ISupplyRequestService supplyRequestService;
    @Autowired
    private ISupplyDeliveryService supplyDeliveryService;
    @Autowired
    private IChargeItemDefAppService chargeItemDefAppService;
    @Autowired
    private IInventoryItemService inventoryItemService;
    @Autowired
    private IChargeItemService chargeItemService;
    @Autowired
    private IInventoryManageService inventoryManageService;
    @Autowired
    private TokenService tokenService;

    /**
     * 审批通过
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PostMapping("/approved")
    public R<?> approved(@RequestParam String busNo, HttpServletRequest request) {
        // 获取登录者的信息
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 获取当前时间
        Date now = DateUtils.getNowDate();

        // 审批单据并返回单据详情
        List<SupplyRequest> agreedList = supplyRequestService.agreeRequest(busNo, loginUser, now);
        if (agreedList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 根据单据，发放物品
        List<SupplyDelivery> deliveredList = supplyDeliveryService.createCompletedSupplyDelivery(agreedList, now);
        if (deliveredList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 获取供应项目所在表
        String itemTable = supplyRequestService.getItemTable(agreedList);
        // 获取供应的物品
        List<Long> itemIdList = supplyRequestService.getItem(agreedList);
        // 获取项目价格相关信息
        List<ItemChargeDetailDto> chargeDetailList = inventoryManageService.getItemChargeDetail(itemIdList);
        List<ChargeItemDefApp> chargeItemDefAppList = new ArrayList<>();
        if (!chargeDetailList.isEmpty()) {
            // 入库项价格验证（验证项：单位价格，批次号）
            chargeItemDefAppList = inventoryManageService.verifyItemCharge(agreedList, chargeDetailList);
        }
        if (!chargeItemDefAppList.isEmpty()) {
            for (ChargeItemDefApp chargeItemDefApp : chargeItemDefAppList) {
                // 增加项目定价子表数据
                chargeItemDefAppService.addChargeItemDefApp(chargeItemDefApp);
            }
        }

        // 查询供应项目的详细信息
        List<SupplyItemDetailDto> supplyItemDetailList = inventoryManageService.getSupplyItemDetail(busNo, itemTable);
        if (!supplyItemDetailList.isEmpty()) {
            // 将供应项目的详细信息装配为库存项目和采购账单
            Pair<List<ChargeItem>, List<InventoryItem>> listPair =
                InventoryManageAssembler.assembleChargeAndInventory(supplyItemDetailList, now);
            if (listPair != null) {
                // 创建已结算的采购财务流水
                chargeItemService.createBilledPurchaseCharge(listPair.getLeft());
                // 入库
                inventoryItemService.stockIn(listPair.getRight());
            }
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 审批驳回
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PostMapping("/reject")
    public R<?> reject(@RequestParam String busNo, HttpServletRequest request) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 获取登录者的信息
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 驳回单据
        boolean result = supplyRequestService.rejectRequest(busNo, loginUser, now);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }
}
