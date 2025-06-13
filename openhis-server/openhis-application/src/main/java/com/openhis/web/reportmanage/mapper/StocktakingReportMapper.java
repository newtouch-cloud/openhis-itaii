/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.reportmanage.dto.StocktakingReportPageDto;
import com.openhis.web.reportmanage.dto.StocktakingReportSearchParam;

/**
 * 库存盘点明细查询用 mapper
 *
 * @author GYY
 * @date 2025-04-16
 */
@Repository
public interface StocktakingReportMapper {

    /**
     * 库存盘点调拨明细
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @param stocktakingReport 单据类型：商品盘点
     * @param stocktakingBatchReport 单据类型：商品批量盘点
     * @param supplyStatus 单据状态：同意
     * @param deliveryStatus 发放状态：已完成
     * @return 库存盘点明细
     */
    Page<StocktakingReportPageDto> selectStocktakingReportPage(@Param("page") Page<StocktakingReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<StocktakingReportSearchParam> queryWrapper,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("stocktakingReport") Integer stocktakingReport,
        @Param("stocktakingBatchReport") Integer stocktakingBatchReport, @Param("supplyStatus") Integer supplyStatus,
        @Param("deliveryStatus") Integer deliveryStatus);
}
