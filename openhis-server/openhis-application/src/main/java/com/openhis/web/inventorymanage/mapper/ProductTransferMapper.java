/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.inventorymanage.dto.SupplySearchParam;
import com.openhis.web.inventorymanage.dto.ProductTransferDetailDto;
import com.openhis.web.inventorymanage.dto.ProductTransferPageDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * @param productTransfer 单据类型：采购入库
     * @return 商品调拨单据分页列表
     */
    Page<ProductTransferPageDto> selectProductTransferPage(@Param("page") Page<ProductTransferPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<SupplySearchParam> queryWrapper,
        @Param("productTransfer") Integer productTransfer);

    /**
     * 查询单据详情
     * 
     * @param busNo 单据号
     * @return 单据详情
     */
    List<ProductTransferDetailDto> selectDetail(@Param("busNo") String busNo);
}
