/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import com.openhis.web.inventorymanage.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 采购入库查询用 mapper
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
     * @return 采购入库分页列表
     */
    Page<PurchaseInDetailDto> selectPurchaseInDetailsPage(@Param("page") Page<PurchaseInDetailDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<PurchaseInSearchParam> queryWrapper);

    /**
     * 查询领用出库分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 领用出库分页列表
     */
    Page<RequisitionOutDetailDto> selectRequisitionOutDetailsPage(@Param("page") Page<RequisitionOutDetailDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<RequisitionOutSearchParam> queryWrapper);

    /**
     * 查询商品调拨分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 商品调拨分页列表
     */
    Page<InventoryTransferDetailDto> selectInventoryTransferDetailsPage(@Param("page") Page<InventoryTransferDetailDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<RequisitionOutSearchParam> queryWrapper);

    /**
     * 查询商品盘点分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 商品盘点分页列表
     */
    Page<InventoryStockTakeDetailDto> selectInventoryStockTakeDetailsPage(@Param("page") Page<InventoryTransferDetailDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<RequisitionOutSearchParam> queryWrapper);
}
