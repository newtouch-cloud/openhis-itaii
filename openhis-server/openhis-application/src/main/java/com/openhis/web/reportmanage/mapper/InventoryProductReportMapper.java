/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.reportmanage.dto.InventoryProductReportPageDto;
import com.openhis.web.reportmanage.dto.InventoryProductReportSearchParam;

/**
 * 库存商品明细查询用 mapper
 *
 * @author GYY
 * @date 2025-04-21
 */
@Repository
public interface InventoryProductReportMapper {

    /**
     * 库存商品明细
     * 
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param lotNumber 命中条件枚举类型：产品批号
     * @return 库存商品明细
     */
    Page<InventoryProductReportPageDto> selectProductReportPage(@Param("page") Page<InventoryProductReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<InventoryProductReportSearchParam> queryWrapper,
        @Param("lotNumber") String lotNumber);
}
