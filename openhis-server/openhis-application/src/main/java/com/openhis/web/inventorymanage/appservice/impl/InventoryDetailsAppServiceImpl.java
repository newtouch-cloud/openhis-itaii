/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.inventorymanage.appservice.IInventoryDetailsAppService;
import com.openhis.web.inventorymanage.dto.*;
import com.openhis.web.inventorymanage.mapper.InventoryDetailsMapper;

/**
 * 采购入库明细查询 impl
 *
 * @author
 * @date 2025-03-10
 */
@Service
public class InventoryDetailsAppServiceImpl implements IInventoryDetailsAppService {

    @Autowired
    private InventoryDetailsMapper inventoryDetailsMapper;

    /**
     * 采购入库明细查询
     *
     * @param purchaseInSearchParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 采购入库明细分页列表
     */
    @Override
    public IPage<PurchaseInDetailDto> purchaseInGetPage(PurchaseInSearchParam purchaseInSearchParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        // QueryWrapper<SupplyRequest> queryWrapper =
        // HisQueryUtils.buildQueryWrapper(inventorySearchParam, searchKey, searchFields, request);
        // 查询入库单据分页列表
        // Page<InventoryReceiptPageDto> inventoryReceiptPage = purchaseInventoryMapper.selectInventoryReceiptPage(
        // new Page<>(pageNo, pageSize), queryWrapper, SupplyType.PURCHASE_INVENTORY.getValue());
        // return R.ok(inventoryReceiptPage);

        QueryWrapper<PurchaseInSearchParam> queryWrapper = HisQueryUtils.buildQueryWrapper(purchaseInSearchParam,
            searchKey, new HashSet<>(Arrays.asList("bus_no", "item_name", "item_no")), request);
        Page<PurchaseInDetailDto> purchaseInInfo =
            inventoryDetailsMapper.selectPurchaseInDetailsPage(new Page<>(pageNo, pageSize), queryWrapper);

        return purchaseInInfo;
    }

    /**
     * 领用出库明细查询
     *
     * @param requisitionOutSearchParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 领用出库明细分页列表
     */
    @Override
    public IPage<RequisitionOutDetailDto> requisitionOutGetPage(RequisitionOutSearchParam requisitionOutSearchParam,
        String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryWrapper<RequisitionOutSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(requisitionOutSearchParam, searchKey,
                new HashSet<>(Arrays.asList("bus_no", "item_name", "item_no")), request);

        Page<RequisitionOutDetailDto> requisitionOutInfo =
            inventoryDetailsMapper.selectRequisitionOutDetailsPage(new Page<>(pageNo, pageSize), queryWrapper);

        return requisitionOutInfo;
    }

    /**
     * 商品调拨明细查询
     *
     * @param requisitionOutSearchParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 商品调拨明细分页列表
     */
    @Override
    public IPage<InventoryTransferDetailDto> inventoryTransferGetPage(
        RequisitionOutSearchParam requisitionOutSearchParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request) {

        QueryWrapper<RequisitionOutSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(requisitionOutSearchParam, searchKey,
                new HashSet<>(Arrays.asList("bus_no", "item_name", "item_no")), request);

        Page<InventoryTransferDetailDto> inventoryTransferInfo =
            inventoryDetailsMapper.selectInventoryTransferDetailsPage(new Page<>(pageNo, pageSize), queryWrapper);

        return inventoryTransferInfo;
    }

    /**
     * 商品调拨明细查询
     *
     * @param requisitionOutSearchParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 商品调拨明细分页列表
     */
    @Override
    public IPage<InventoryStockTakeDetailDto> inventoryStockTakeGetPage(
        RequisitionOutSearchParam requisitionOutSearchParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request) {

        QueryWrapper<RequisitionOutSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(requisitionOutSearchParam, searchKey,
                new HashSet<>(Arrays.asList("bus_no", "item_name", "item_no")), request);

        Page<InventoryStockTakeDetailDto> inventoryStockTakeInfo =
            inventoryDetailsMapper.selectInventoryStockTakeDetailsPage(new Page<>(pageNo, pageSize), queryWrapper);

        return inventoryStockTakeInfo;
    }

}
