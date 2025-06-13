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
 * 商品盘点查询用 mapper
 *
 * @author zwh
 * @date 2025-03-10
 */
@Repository
public interface ProductStocktakingMapper {

    /**
     * 查询盘点单据分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param productStocktaking 单据类型：商品盘点
     * @param BatchStocktaking 单据类型：商品批量盘点
     * @return 盘点单据分页列表
     */
    Page<ReceiptPageDto> selectStocktakingReceiptPage(@Param("page") Page<ReceiptPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<ProductStocktakingSearchParam> queryWrapper,
        @Param("productStocktaking") Integer productStocktaking, @Param("BatchStocktaking") Integer BatchStocktaking);

    /**
     * 查询单据详情
     *
     * @param page 分页
     * @param busNo 单据号
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @return 单据详情
     */
    Page<ReceiptDetailDto> selectDetail(@Param("page") Page<ProductTransferDetailDto> page,
        @Param("busNo") String busNo, @Param("medicine") Integer medicine, @Param("device") Integer device,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName);;

    /**
     * 生成批量盘点单
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param sourceLocationId 盘点仓库id
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @return 入库单据分页列表
     */
    Page<ReceiptDetailDto> selectStocktakingReceiptDetail(@Param("page") Page<ReceiptPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<StocktakingBatchSearchParam> queryWrapper,
        @Param("sourceLocationId") Long sourceLocationId, @Param("medicationTableName") String medicationTableName,
        @Param("deviceTableName") String deviceTableName, @Param("medicine") Integer medicine,
        @Param("device") Integer device, @Param("inventoryStatusEnum") Integer inventoryStatusEnum);
}
