/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import java.util.List;

import com.openhis.web.inventorymanage.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 商品调拨查询用 mapper
 *
 * @author zwh
 * @date 2025-03-10
 */
@Repository
public interface ProductTransferMapper {

    /**
     * 查询商品调拨单据分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param productTransfer 单据类型：商品调拨
     * @param productBatchTransfer 单据类型：商品批量调拨
     * @return 商品调拨单据分页列表
     */
    Page<ProductTransferPageDto> selectProductTransferPage(@Param("page") Page<ProductTransferPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<SupplySearchParam> queryWrapper,
        @Param("productTransfer") Integer productTransfer, @Param("productBatchTransfer") Integer productBatchTransfer);

    /**
     * 查询批量单据详情
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param sourceLocationId 源仓库id
     * @param purposeLocationId 目的仓库id
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @return 商品批量调拨单据分页列表
     */
    Page<ProductTransferDetailDto> selectBatchTransferDetail(@Param("page") Page<ProductTransferDetailDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<SupplySearchParam> queryWrapper,
        @Param("sourceLocationId") Long sourceLocationId, @Param("purposeLocationId") Long purposeLocationId,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("medicine") Integer medicine, @Param("device") Integer device,
        @Param("inventoryStatusEnum") Integer inventoryStatusEnum);

    /**
     * 查询单据详情
     *
     * @param page 分页
     * @param busNo 单据号
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @return 单据详情
     */
    Page<ProductTransferDetailDto> selectDetail(@Param("page") Page<ProductTransferDetailDto> page,
        @Param("busNo") String busNo, @Param("medicationTableName") String medicationTableName,
        @Param("deviceTableName") String deviceTableName, @Param("medicine") Integer medicine,
        @Param("device") Integer device);
}
