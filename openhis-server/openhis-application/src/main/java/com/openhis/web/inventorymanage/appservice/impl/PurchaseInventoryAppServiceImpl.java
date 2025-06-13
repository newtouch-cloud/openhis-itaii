/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.core.common.exception.ServiceException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
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

    @Resource
    private PurchaseInventoryMapper purchaseInventoryMapper;

    @Resource
    private ISupplyRequestService supplyRequestService;

    @Resource
    private ISupplierService supplierService;

    @Resource
    private IPractitionerService practitionerService;

    @Resource
    private AssignSeqUtil assignSeqUtil;

    /**
     * 入库单据页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> purchaseInventoryInit() {

        PurchaseInventoryInitDto initDto = new PurchaseInventoryInitDto();
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
        // 单据类型
        List<PurchaseInventoryInitDto.supplyTypeOption> supplyTypeOptions = new ArrayList<>();
        supplyTypeOptions.add(new PurchaseInventoryInitDto.supplyTypeOption(SupplyType.PURCHASE_INVENTORY.getValue(),
            SupplyType.PURCHASE_INVENTORY.getInfo()));
        supplyTypeOptions.add(new PurchaseInventoryInitDto.supplyTypeOption(SupplyType.PRODUCT_RETURN.getValue(),
            SupplyType.PRODUCT_RETURN.getInfo()));

        initDto.setSupplierListOptions(supplierListOptions).setPractitionerListOptions(practitionerListOptions)
            .setSupplyStatusOptions(supplyStatusOptions).setSupplyTypeOptions(supplyTypeOptions);

        return R.ok(initDto);
    }

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> purchaseNoInit() {
        PurchaseInventoryInitDto initDto = new PurchaseInventoryInitDto();
        // 单据号
        initDto.setBusNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.PURCHASE_NUM.getPrefix(), 10));
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
        Page<ReceiptPageDto> inventoryReceiptPage =
            purchaseInventoryMapper.selectInventoryReceiptPage(new Page<>(pageNo, pageSize), queryWrapper,
                SupplyType.PURCHASE_INVENTORY.getValue());

        inventoryReceiptPage.getRecords().forEach(e -> {
            // 单据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, e.getStatusEnum()));
            // 仓库类型
            e.setPurposeTypeEnum_enumText(EnumUtils.getInfoByValue(LocationForm.class, e.getPurposeTypeEnum()));
            // 单据类型
            e.setTypeEnum_enumText(EnumUtils.getInfoByValue(SupplyType.class, e.getTypeEnum()));
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
     * 添加/编辑入库单据(批量)
     *
     * @param purchaseInventoryDtoList 入库单据
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditInventoryReceipt(List<PurchaseInventoryDto> purchaseInventoryDtoList) {

        // 校验(已经审批通过的单号(请求状态是同意),不能再重复编辑请求)
        boolean validation = supplyRequestService.supplyRequestValidation(purchaseInventoryDtoList.get(0).getBusNo());
        if(validation){
            throw new ServiceException("请勿重复审批");
        }

        List<String> idList = new ArrayList<>();

        // 单据号取得
        List<String> busNoList =
                purchaseInventoryDtoList.stream().map(PurchaseInventoryDto::getBusNo).collect(Collectors.toList());
        // 请求数据取得
        List<SupplyRequest> requestList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
        if (!requestList.isEmpty()) {
            // 请求id取得
            List<Long> requestIdList = requestList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
            // 单据信息删除
            supplyRequestService.removeByIds(requestIdList);
        }

        List<SupplyRequest> supplyRequestList = new ArrayList<>();
        for (PurchaseInventoryDto purchaseInventoryDto : purchaseInventoryDtoList) {
            // 初始化单据信息
            SupplyRequest supplyRequest = new SupplyRequest();
            BeanUtils.copyProperties(purchaseInventoryDto, supplyRequest);

            // 生成待发送的入库单据
            supplyRequest
                    // id
                    .setId(null)
                    // 单据分类：非库存供应
                    .setCategoryEnum(SupplyCategory.NON_STOCK.getValue())
                    // 单据类型：采购入库
                    .setTypeEnum(SupplyType.PURCHASE_INVENTORY.getValue())
                    // 制单人
                    .setApplicantId(SecurityUtils.getLoginUser().getPractitionerId())
                    // 申请时间
                    .setApplyTime(DateUtils.getNowDate());
            supplyRequestList.add(supplyRequest);

        }

        // 保存
        supplyRequestService.saveOrUpdateBatch(supplyRequestList);

        // 请求id取得
        List<SupplyRequest> supplyRequestIdList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
        // 返回请求id列表
        List<Long> requestIdList =
                supplyRequestIdList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
        for (Long list : requestIdList) {
            idList.add(list.toString());
        }

        // 返回请求id
        return R.ok(idList, null);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @Override
    public R<?> deleteReceipt(List<Long> supplyRequestIds) {
        // 删除单据
        boolean result = supplyRequestService.removeByIds(supplyRequestIds);
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

        // 校验(已经审批通过的单号(请求状态是同意),不能再重复编辑请求)
        boolean validation = supplyRequestService.supplyRequestValidation(busNo);
        if(validation){
            throw new ServiceException("请勿重复审批");
        }
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
