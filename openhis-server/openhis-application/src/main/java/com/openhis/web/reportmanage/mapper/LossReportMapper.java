/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import com.openhis.web.reportmanage.dto.LossReportPageDto;
import com.openhis.web.reportmanage.dto.LossReportSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.reportmanage.dto.StocktakingReportPageDto;
import com.openhis.web.reportmanage.dto.StocktakingReportSearchParam;

/**
 * 报损明细查询用 mapper
 *
 * @author ym
 * @date 2025-05-21
 */
@Repository
public interface LossReportMapper {

    /**
     * 报损明细
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @param lossReport 单据类型：报损
     * @param supplyStatus 单据状态：同意
     * @param deliveryStatus 发放状态：已完成
     * @return 报损明细
     */
    Page<LossReportPageDto> selectLossReportPage(@Param("page") Page<LossReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<LossReportSearchParam> queryWrapper,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("lossReport") Integer lossReport, @Param("supplyStatus") Integer supplyStatus,
        @Param("deliveryStatus") Integer deliveryStatus);
}
