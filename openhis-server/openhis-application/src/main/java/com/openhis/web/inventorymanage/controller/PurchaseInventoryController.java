/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.SupplyCategory;
import com.openhis.common.enums.SupplyType;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.inventorymanage.appservice.IInventoryManageService;
import com.openhis.web.inventorymanage.dto.InventoryReceiptDto;
import com.openhis.web.inventorymanage.dto.InventoryReceiptPageDto;
import com.openhis.web.inventorymanage.dto.InventorySearchParam;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.mapper.SupplyRequestMapper;
import com.openhis.workflow.service.ISupplyRequestService;

import lombok.extern.slf4j.Slf4j;

/**
 * 采购入库 controller
 *
 * @author zwh
 * @date 2025-02-18
 */
@RestController
@RequestMapping("/inventory-manage/purchase")
@Slf4j
public class PurchaseInventoryController {

    @Autowired
    private ISupplyRequestService supplyRequestService;
    @Autowired
    private IInventoryManageService inventoryManageService;
    @Autowired
    private SupplyRequestMapper supplyRequestMapper;

    // @GetMapping(value = "/init")
    // public R<?> init() {
    // // 采番
    //
    // InventoryReceiptInitDto initDto = new InventoryReceiptInitDto();
    // // 设置供应商列表
    // initDto.setSupplier(supplierService.list())
    // // 设置药房列表
    // .setLocation(locationService.list(new LambdaQueryWrapper<Location>().in(Location::getFormEnum, 1)))
    // // 药品详细
    // .setMedicationDetail(medicationService.getDetailList());
    // return R.ok(initDto);
    // }

    // 添加入库单据之前需要
    // 1.supplier供应商信息列表
    // 2.location信息列表包括（药房，药库，材料柜，护理站）
    // 3.practitioner_role与practitioner联查获取对应location的管理员列表
    // 4.查询选定对应药品类型的药品信息列表

    /**
     * 入库单据列表
     *
     * @param inventorySearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 入库单据分页列表
     */
    @GetMapping(value = "/inventory-receipt-page")
    public R<?> getPage(InventorySearchParam inventorySearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {

        // 按照单据号groupBy
        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.BusNo);

        // 构建查询条件
        QueryWrapper<SupplyRequest> queryWrapper =
            HisQueryUtils.buildQueryWrapper(inventorySearchParam, searchKey, searchFields, request);
        // 查询入库单据分页列表
        Page<InventoryReceiptPageDto> inventoryReceiptPage =
            HisPageUtils.selectPage(supplyRequestMapper, queryWrapper, pageNo, pageSize, InventoryReceiptPageDto.class);

        return R.ok(inventoryReceiptPage);
    }

    /**
     * 入库单据详情
     * 
     * @param busNo 单据号
     * @return 入库单据详情
     */
    public R<?> getDetail(@RequestParam String busNo) {

        //
        return R.ok();
    }

    /**
     * 添加/编辑入库单据
     *
     * @param inventoryReceiptDto 入库单据
     * @return 操作结果
     */
    @PostMapping("/inventory-receipt")
    public R<?> addOrEditInventoryReceipt(@Validated @RequestBody InventoryReceiptDto inventoryReceiptDto) {

        // 初始化单据信息
        SupplyRequest supplyRequest = new SupplyRequest();
        BeanUtils.copyProperties(inventoryReceiptDto, supplyRequest);

        // // 业务校验
        // R<?> result = purchaseInventoryService.verifyInventoryReceipt(supplyRequest);
        // // 校验失败返回提示信息
        // if (result.getCode() == HttpStatus.ERROR) {
        // return result;
        // }

        if (inventoryReceiptDto.getId() != null) {
            // 更新单据信息
            supplyRequestService.updateById(supplyRequest);
        } else {
            // 生成待发送的入库单据
            supplyRequest
                // 单据分类：非库存供应
                .setCategoryEnum(SupplyCategory.NON_STOCK.getValue())
                // 单据类型：采购入库
                .setTypeEnum(SupplyType.PURCHASE_INVENTORY.getValue())
                // 申请时间
                .setApplyTime(DateUtils.getNowDate());
            supplyRequestService.save(supplyRequest);
        }
        // 返回单据id
        return R.ok(supplyRequest.getId(), null);
    }

    /**
     * 删除方法
     *
     * @param supplyRequestId 主表id
     * @return 操作结果
     */
    @DeleteMapping("/inventory-receipt")
    public R<?> deleteInventoryReceipt(@RequestParam Long supplyRequestId) {
        // 删除单据
        boolean result = supplyRequestService.removeById(supplyRequestId);
        return result ? R.ok() : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     */
    @PutMapping("/submit-approval")
    public R<?> submitApproval(@RequestParam String busNo) {
        // 单据提交审核
        boolean result = supplyRequestService.submitApproval(busNo);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @PutMapping("/withdraw-approval")
    public R<?> withdrawApproval(@RequestParam String busNo) {
        // 撤回审核
        boolean result = supplyRequestService.withdrawApproval(busNo);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }
}
