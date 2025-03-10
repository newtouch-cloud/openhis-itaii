/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.inventorymanage.appservice.IPurchaseInventoryAppService;
import com.openhis.web.inventorymanage.dto.InventoryReceiptDto;
import com.openhis.web.inventorymanage.dto.InventoryReceiptPageDto;
import com.openhis.web.inventorymanage.dto.InventorySearchParam;
import com.openhis.web.inventorymanage.mapper.PurchaseInventoryMapper;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.mapper.SupplyRequestMapper;
import com.openhis.workflow.service.ISupplyRequestService;

/**
 * 采购入库 impl
 *
 * @author zwh
 * @date 2025-03-08
 */
@Service
public class PurchaseInventoryAppServiceImpl implements IPurchaseInventoryAppService {

    @Autowired
    private SupplyRequestMapper supplyRequestMapper;

    @Autowired
    private PurchaseInventoryMapper purchaseInventoryMapper;

    @Autowired
    private ISupplyRequestService supplyRequestService;

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
    @Override
    public R<?> getPage(InventorySearchParam inventorySearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.BusNo);

        // 构建查询条件
        QueryWrapper<SupplyRequest> queryWrapper =
            HisQueryUtils.buildQueryWrapper(inventorySearchParam, searchKey, searchFields, request);
        // 查询入库单据分页列表
        Page<InventoryReceiptPageDto> inventoryReceiptPage = purchaseInventoryMapper.selectInventoryReceiptPage(
            new Page<>(pageNo, pageSize), queryWrapper, SupplyType.PURCHASE_INVENTORY.getValue());
        return R.ok(inventoryReceiptPage);
    }

    /**
     * 入库单据详情
     *
     * @param busNo 单据号
     * @return 入库单据详情
     */
    @Override
    public R<?> getDetail(String busNo) {
        return null;
    }

    /**
     * 添加/编辑入库单据
     *
     * @param inventoryReceiptDto 入库单据
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditInventoryReceipt(InventoryReceiptDto inventoryReceiptDto) {

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
    @Override
    public R<?> deleteInventoryReceipt(Long supplyRequestId) {
        // 删除单据
        boolean result = supplyRequestService.removeById(supplyRequestId);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> submitApproval(String busNo) {
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
    @Override
    public R<?> withdrawApproval(String busNo) {
        // 撤回审核
        boolean result = supplyRequestService.withdrawApproval(busNo);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }
}
