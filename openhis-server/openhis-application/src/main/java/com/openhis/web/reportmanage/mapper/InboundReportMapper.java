/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.reportmanage.dto.InboundReportPageDto;
import com.openhis.web.reportmanage.dto.InboundReportSearchParam;

/**
 * 入库明细查询用 mapper
 *
 * @author GYY
 * @date 2025-04-22
 */
@Repository
public interface InboundReportMapper {

    /**
     * 入库商品明细
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @param purchaseInventory 单据类型：采购入库
     * @param supplyStatus 单据状态：同意
     * @param deliveryStatus 发放状态：已完成
     * @return 入库商品明细
     */
    Page<InboundReportPageDto> selectInboundReportPage(@Param("page") Page<InboundReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<InboundReportSearchParam> queryWrapper,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("purchaseInventory") Integer purchaseInventory, @Param("supplyStatus") Integer supplyStatus,
        @Param("deliveryStatus") Integer deliveryStatus);
}
