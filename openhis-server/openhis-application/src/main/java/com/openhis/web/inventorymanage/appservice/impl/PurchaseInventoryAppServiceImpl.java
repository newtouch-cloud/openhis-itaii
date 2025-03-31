/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AssignSeqEnum;
import com.openhis.common.enums.SupplyCategory;
import com.openhis.common.enums.SupplyStatus;
import com.openhis.common.enums.SupplyType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.inventorymanage.appservice.IPurchaseInventoryAppService;
import com.openhis.web.inventorymanage.dto.*;
import com.openhis.web.inventorymanage.mapper.PurchaseInventoryMapper;
import com.openhis.workflow.domain.SupplyRequest;
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
    private PurchaseInventoryMapper purchaseInventoryMapper;

    @Autowired
    private ISupplyRequestService supplyRequestService;

    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private IPractitionerService practitionerService;

    @Autowired
    private AssignSeqUtil assignSeqUtil;

    /**
     * 入库单据页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> purchaseInventoryInit() {

        PurchaseInventoryInitDto initDto = new PurchaseInventoryInitDto();
        // 单据号
        initDto.setBusNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.PURCHASE_NUM.getPrefix(), 12));
        // 查询供应商列表
        List<Supplier> supplierList = supplierService.getList();
        // 查询经手人列表
        List<Practitioner> practitionerList = practitionerService.getList();
        // 经手人信息
        List<PurchaseInventoryInitDto.practitionerListOption> practitionerListOptions = practitionerList.stream()
            .map(practitioner -> new PurchaseInventoryInitDto.practitionerListOption(practitioner.getId(),
                practitioner.getName()))
            .collect(Collectors.toList());
        // 供应商信息
        List<PurchaseInventoryInitDto.supplierListOption> supplierListOptions = supplierList.stream()
            .map(supplier -> new PurchaseInventoryInitDto.supplierListOption(supplier.getId(), supplier.getName()))
            .collect(Collectors.toList());
        // 审批状态
        List<PurchaseInventoryInitDto.supplyStatusOption> supplyStatusOptions = Stream.of(SupplyStatus.values())
            .map(supplyStatus -> new PurchaseInventoryInitDto.supplyStatusOption(supplyStatus.getValue(),
                supplyStatus.getInfo()))
            .collect(Collectors.toList());

        initDto.setSupplierListOptions(supplierListOptions).setPractitionerListOptions(practitionerListOptions)
            .setSupplyStatusOptions(supplyStatusOptions);

        return R.ok(initDto);
    }

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
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);

        // 构建查询条件
        QueryWrapper<InventorySearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(inventorySearchParam, searchKey, searchFields, request);
        // 查询入库单据分页列表
        Page<ReceiptPageDto> inventoryReceiptPage = purchaseInventoryMapper.selectInventoryReceiptPage(
            new Page<>(pageNo, pageSize), queryWrapper, SupplyType.PURCHASE_INVENTORY.getValue());

        inventoryReceiptPage.getRecords().forEach(e -> {
            // 单据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, e.getStatusEnum()));
        });
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
        List<ReceiptDetailDto> receiptDetailList = purchaseInventoryMapper.selectDetail(busNo);
        if (receiptDetailList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        return R.ok(receiptDetailList);
    }

    /**
     * 添加/编辑入库单据
     *
     * @param purchaseInventoryDto 入库单据
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditInventoryReceipt(PurchaseInventoryDto purchaseInventoryDto) {

        // 初始化单据信息
        SupplyRequest supplyRequest = new SupplyRequest();
        BeanUtils.copyProperties(purchaseInventoryDto, supplyRequest);

        if (purchaseInventoryDto.getId() != null) {
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
     * 删除单据
     *
     * @param supplyRequestId 供应请求id
     * @return 操作结果
     */
    @Override
    public R<?> deleteReceipt(Long supplyRequestId) {
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
