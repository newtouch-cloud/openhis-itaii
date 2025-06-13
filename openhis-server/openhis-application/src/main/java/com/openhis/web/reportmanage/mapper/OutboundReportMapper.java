/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import com.openhis.web.reportmanage.dto.OutboundReportSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.reportmanage.dto.OutboundReportPageDto;

/**
 * 出库明细查询用 mapper
 *
 * @author yuanzs
 * @date 2025-04-21
 */
@Repository
public interface OutboundReportMapper {

    /**
     * 出库明细查询
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @param issueInventory 单据类型：领用出库
     * @param supplyStatus 单据状态：同意
     * @param deliveryStatus 发放状态：已完成
     * @return 出库明细
     */
    Page<OutboundReportPageDto> selectOutboundReportPage(@Param("page") Page<OutboundReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<OutboundReportSearchParam> queryWrapper,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("issueInventory") Integer issueInventory, @Param("supplyStatus") Integer supplyStatus,
        @Param("deliveryStatus") Integer deliveryStatus);
}
