/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.inventorymanage.dto.InventoryDetailsPageDto;
import com.openhis.web.inventorymanage.dto.InventoryDetailsSearchParam;

/**
 * 库存相关明细查询用 mapper
 *
 * @author
 * @date 2025-03-10
 */
@Repository
public interface InventoryDetailsMapper {

    /**
     * 查询采购入库分页列表
     * 
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param purchaseInventory 单据类型：采购入库
     * @param supplyStatus 单据状态：同意
     * @return 采购入库分页列表
     */
    Page<InventoryDetailsPageDto> selectPurchaseInDetailsPage(@Param("page") Page<InventoryDetailsPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<InventoryDetailsSearchParam> queryWrapper,
        @Param("purchaseInventory") Integer purchaseInventory, @Param("supplyStatus") Integer supplyStatus);

    /**
     * 查询商品调拨分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param productTransfer 单据类型：商品调拨
     * @param supplyStatus 单据状态：同意
     * @return 商品调拨分页列表
     */
    Page<InventoryDetailsPageDto> selectTransferDetailsPage(@Param("page") Page<InventoryDetailsPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<InventoryDetailsSearchParam> queryWrapper,
        @Param("productTransfer") Integer productTransfer, @Param("supplyStatus") Integer supplyStatus);

    /**
     * 查询领用出库分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param issueInventory 单据类型：领用出库
     * @param agree 单据状态：同意
     * @return 领用出库分页列表
     */
    Page<InventoryDetailsPageDto> selectRequisitionOutDetailsPage(@Param("page") Page<InventoryDetailsPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<InventoryDetailsSearchParam> queryWrapper,
        @Param("issueInventory") Integer issueInventory, @Param("agree") Integer agree);

    /**
     * 查询商品盘点分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param productStocktaking 单据类型：商品盘点
     * @param agree 单据状态：同意
     * @param approval 单据状态：审核中
     * @return 商品盘点分页列表
     */
    Page<InventoryDetailsPageDto> selectInventoryStockDetailsPage(@Param("page") Page<InventoryDetailsPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<InventoryDetailsSearchParam> queryWrapper,
        @Param("productStocktaking") Integer productStocktaking, @Param("agree") Integer agree,
        @Param("approval") Integer approval);
}
