/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.inventorymanage.dto.ProductDetailsPageDto;
import com.openhis.web.inventorymanage.dto.ProductDetailsSearchParam;

/**
 * 库存商品明细查询用 mapper
 *
 * @author yuanzs
 * @date 2025-04-25
 */
@Repository
public interface ProductDetailsMapper {

    /**
     * 查询库存商品明细分页列表
     * 
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 库存商品明细分页列表
     */
    Page<ProductDetailsPageDto> selectProductDetailsPage(@Param("page") Page<ProductDetailsPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<ProductDetailsSearchParam> queryWrapper);

}
