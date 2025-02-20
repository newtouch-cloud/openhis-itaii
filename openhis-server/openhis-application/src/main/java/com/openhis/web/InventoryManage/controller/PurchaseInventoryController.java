/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.InventoryManage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.InventoryManage.dto.SupplyRequestDto;
import com.openhis.web.InventoryManage.dto.SupplySaveRequestDto;
import com.openhis.web.inventoryManage.dto.SupplySearchParam;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.ISupplyRequestService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 采购入库 controller
 *
 * @author zwh
 * @date 2025-02-18
 */
@RestController
@RequestMapping("/purchase-inventory")
@Slf4j
@AllArgsConstructor
public class PurchaseInventoryController {

    private final ISupplyRequestService supplyRequestService;

    private final IChargeItemService chargeItemService;

    @GetMapping(value = "/test")
    public R<?> test() {
        // return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002,new Object[] {"12345"})) ;
        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"12345"}));
    }

    /**
     * 入库单据分页列表
     *
     * @param supplySearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 入库单据分页列表
     */
    @GetMapping(value = "/inventory-receipt-page")
    public Page<SupplyRequest> getPage(SupplySearchParam supplySearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        // 查询supply_request相关信息并返回分页列表

        return supplyRequestService.page(new Page<>(pageNo, pageSize));
    }

    // 添加入库单据之前需要
    // 1.supplier供应商信息列表
    // 2.location信息列表包括（药房，药库，材料柜，护理站）
    // 3.practitioner_role与practitioner联查获取对应location的管理员列表
    // 4.查询选定对应药品类型的药品信息列表

    /**
     * 添加入库单据（生成供应请求）
     *
     * @param supplyRequestDto 供应请求信息
     */
    @PostMapping("/add-supply-request")
    public R<?> addSupplyRequest(@Validated @RequestBody SupplyRequestDto supplyRequestDto) {
        // 生成待发送的入库单据supply_request
        SupplyRequest supplyRequest = new SupplyRequest();
        BeanUtils.copyProperties(supplyRequestDto, supplyRequest);
        // 如果业务上不需要其它处理 直接调用service的保存方法
        boolean saveSupplyRequestSuccess = supplyRequestService.save(supplyRequest);

        if (!saveSupplyRequestSuccess) {
            return R.fail();
        }
        // 生成收费项目charge_item
        ChargeItem chargeItem = new ChargeItem();
        // 如果字段很少建议手动set赋值
        chargeItem.setUnitPrice(supplyRequestDto.getUnitPrice());
        boolean saveChargeItemSuccess = chargeItemService.saveChargeItem(chargeItem);
        // 如果采购单价被修改了，需要根据批次号更新采购单价子表价格、

        // if (saveChargeItemSuccess) {
        // return R.ok();
        // } else {
        // return R.fail();
        // }
        return saveChargeItemSuccess ? R.ok() : R.fail();
    }

    /**
     * 编辑入库单据
     *
     * @param supplySaveRequestDto 供应请求信息
     */
    @PutMapping("/edit-supply-request")
    public R<?> editSupplyRequest(@Validated @RequestBody SupplySaveRequestDto supplySaveRequestDto) {
        // 更新supply_request信息
        SupplyRequest saveRequest = new SupplyRequest();
        BeanUtils.copyProperties(supplySaveRequestDto, saveRequest);
        if (!supplyRequestService.updateById(saveRequest)) {
            return R.fail();
        }
        // 更新收费项目charge_item
        ChargeItem chargeItem = new ChargeItem();
        BeanUtils.copyProperties(supplySaveRequestDto, chargeItem);
        chargeItem.setId(supplySaveRequestDto.getChargeItemId());
        return chargeItemService.updateChargeItem(chargeItem) ? R.ok() : R.fail();
    }

    /**
     * 删除方法
     *
     * @param supplyRequestId 主表id
     */
    @DeleteMapping("/delete-supply-request")
    public R<?> deleteSupplyRequest(@RequestParam Long supplyRequestId) {
        // 全都是逻辑删除
        // 通过id将supply_request表的delFlag更新为1
        boolean deleteSuccess = supplyRequestService.update(new LambdaUpdateWrapper<SupplyRequest>()
            .eq(SupplyRequest::getId, supplyRequestId).set(SupplyRequest::getDeleteFlag, 1));

        if (!deleteSuccess) {
            return R.fail();
        }

        boolean deleteChargeItemSuccess = chargeItemService.update(new LambdaUpdateWrapper<ChargeItem>()
            .eq(ChargeItem::getServiceId, supplyRequestId).set(ChargeItem::getDeleteFlag, 1));

        return deleteChargeItemSuccess ? R.ok() : R.fail();
    }

    /**
     * 单据提交申请
     *
     * @param supplyRequest 供应请求信息
     */
    public void submitExamine(SupplyRequest supplyRequest) {

        // 更改供应请求单据状态
        // 生成供应分发supply_delivery
    }
}
