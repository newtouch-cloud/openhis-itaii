/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import com.openhis.web.reportmanage.dto.PurchaseReturnReportPageDto;
import com.openhis.web.reportmanage.dto.PurchaseReturnReportSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 采购退货明细查询用 mapper
 *
 * @author ym
 * @date 2025-05-23
 */
@Repository
public interface PurchaseReturnReportMapper {

    /**
     * 入库商品明细
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @param productReturn 单据类型：采购退货
     * @param supplyStatus 单据状态：同意
     * @param deliveryStatus 发放状态：已完成
     * @return 采购退货商品明细
     */
    Page<PurchaseReturnReportPageDto> selectPurchaseReturnReportPage(@Param("page") Page<PurchaseReturnReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<PurchaseReturnReportSearchParam> queryWrapper,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("productReturn") Integer productReturn, @Param("supplyStatus") Integer supplyStatus,
        @Param("deliveryStatus") Integer deliveryStatus);
}
