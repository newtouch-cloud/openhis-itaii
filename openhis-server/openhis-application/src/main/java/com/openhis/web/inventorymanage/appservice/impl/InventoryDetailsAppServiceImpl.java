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
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.CategoryType;
import com.openhis.common.enums.InventoryType;
import com.openhis.common.enums.SupplyStatus;
import com.openhis.common.enums.SupplyType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.inventorymanage.appservice.IInventoryDetailsAppService;
import com.openhis.web.inventorymanage.dto.InventoryDetailsPageDto;
import com.openhis.web.inventorymanage.dto.InventoryDetailsSearchParam;
import com.openhis.web.inventorymanage.mapper.InventoryDetailsMapper;

/**
 * 库存相关明细查询 impl
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
     * @param inventoryDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 采购入库明细分页列表
     */
    @Override
    public R<?> purchaseInGetPage(InventoryDetailsSearchParam inventoryDetailsSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        // 项目名
        searchFields.add(CommonConstants.FieldName.Name);
        // 项目编码
        searchFields.add(CommonConstants.FieldName.BusNo);
        // 单据号
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);

        // 构建查询条件
        QueryWrapper<InventoryDetailsSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(inventoryDetailsSearchParam, searchKey, searchFields, request);

        // 查询入库明细分页列表
        Page<InventoryDetailsPageDto> purchaseInPage =
            inventoryDetailsMapper.selectPurchaseInDetailsPage(new Page<>(pageNo, pageSize), queryWrapper,
                SupplyType.PURCHASE_INVENTORY.getValue(), SupplyStatus.AGREE.getValue());

        purchaseInPage.getRecords().forEach(e -> {
            // 药品类型
            e.setItemTableText(EnumUtils.getInfoByValue(CategoryType.class, e.getItemTable()));
        });
        return R.ok(purchaseInPage);
    }

    /**
     * 商品调拨明细查询
     *
     * @param inventoryDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨明细分页列表
     */
    @Override
    public R<?> transferGetPage(InventoryDetailsSearchParam inventoryDetailsSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        // 项目名
        searchFields.add(CommonConstants.FieldName.Name);
        // 单据号
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);

        // 构建查询条件
        QueryWrapper<InventoryDetailsSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(inventoryDetailsSearchParam, searchKey, searchFields, request);

        // 查询入库明细分页列表
        Page<InventoryDetailsPageDto> transferPage =
            inventoryDetailsMapper.selectTransferDetailsPage(new Page<>(pageNo, pageSize), queryWrapper,
                SupplyType.PRODUCT_TRANSFER.getValue(), SupplyStatus.AGREE.getValue());

        transferPage.getRecords().forEach(e -> {
            // 药品类型
            e.setItemTableText(EnumUtils.getInfoByValue(CategoryType.class, e.getItemTable()));
            // 源仓库类型
            e.setSourceTypeText(EnumUtils.getInfoByValue(InventoryType.class, e.getItemTable()));
            // 目的仓库类型
            e.setPurposeTypeText(EnumUtils.getInfoByValue(InventoryType.class, e.getItemTable()));
        });
        return R.ok(transferPage);
    }

    /**
     * 领用出库明细查询
     *
     * @param inventoryDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 领用出库明细分页列表
     */
    @Override
    public R<?> requisitionOutGetPage(InventoryDetailsSearchParam inventoryDetailsSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        // 药品名称
        searchFields.add(CommonConstants.FieldName.Name);
        // 编码
        searchFields.add(CommonConstants.FieldName.BusNo);
        // 单据号
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);

        // 构建查询条件
        QueryWrapper<InventoryDetailsSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(inventoryDetailsSearchParam, searchKey, searchFields, request);

        Page<InventoryDetailsPageDto> requisitionOutInfo =
            inventoryDetailsMapper.selectRequisitionOutDetailsPage(new Page<>(pageNo, pageSize), queryWrapper,
                SupplyType.ISSUE_INVENTORY.getValue(), SupplyStatus.AGREE.getValue());

        requisitionOutInfo.getRecords().forEach(e -> {
            // 药品类型
            e.setItemTableText(EnumUtils.getInfoByValue(CategoryType.class, e.getItemTable()));
        });

        return R.ok(requisitionOutInfo);
    }

    /**
     * 商品盘点明细查询
     *
     * @param inventoryDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品盘点明细分页列表
     */
    @Override
    public R<?> inventoryStockGetPage(InventoryDetailsSearchParam inventoryDetailsSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        // 项目名
        searchFields.add(CommonConstants.FieldName.Name);
        // 项目编码
        searchFields.add(CommonConstants.FieldName.BusNo);
        // 单据号
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);

        // 构建查询条件
        QueryWrapper<InventoryDetailsSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(inventoryDetailsSearchParam, searchKey, searchFields, request);

        // 商品盘点明细分页列表
        Page<InventoryDetailsPageDto> purchaseInPage = inventoryDetailsMapper.selectInventoryStockDetailsPage(
            new Page<>(pageNo, pageSize), queryWrapper, SupplyType.PRODUCT_STOCKTAKING.getValue(),
            SupplyStatus.AGREE.getValue(), SupplyStatus.APPROVAL.getValue());

        purchaseInPage.getRecords().forEach(e -> {
            // 药品类型
            e.setItemTableText(EnumUtils.getInfoByValue(CategoryType.class, e.getItemTable()));
        });
        return R.ok(purchaseInPage);
    }

}
