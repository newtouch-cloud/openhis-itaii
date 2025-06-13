/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.reportmanage.dto.TransferReportPageDto;
import com.openhis.web.reportmanage.dto.TransferReportSearchParam;

/**
 * 商品调拨明细查询用 mapper
 *
 * @author GYY
 * @date 2025-04-15
 */
@Repository
public interface TransferReportMapper {

    /**
     * 查询商品调拨明细
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @param transferReport 单据类型：商品调拨
     * @param transferBatchReport 单据类型：商品批量调拨
     * @param supplyStatus 单据状态：同意
     * @param deliveryStatus 发放状态：已完成
     * @return 商品调拨明细
     */
    Page<TransferReportPageDto> selectTransferReportPage(@Param("page") Page<TransferReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<TransferReportSearchParam> queryWrapper,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("transferReport") Integer transferReport, @Param("transferBatchReport") Integer transferBatchReport,
        @Param("supplyStatus") Integer supplyStatus, @Param("deliveryStatus") Integer deliveryStatus);
}
