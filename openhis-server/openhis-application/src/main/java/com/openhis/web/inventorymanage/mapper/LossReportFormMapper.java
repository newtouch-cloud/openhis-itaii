/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.inventorymanage.dto.LossReportFormPageDto;
import com.openhis.web.inventorymanage.dto.LossReportSearchParam;
import com.openhis.web.inventorymanage.dto.ReceiptDetailDto;

/**
 * 报损单查询用 mapper
 *
 * @author gyy
 * @date 2025-04-03
 */
@Repository
public interface LossReportFormMapper {

    /**
     * 查询报损单单据分页列表
     * 
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param lossReportForm 单据类型：报损单
     * @return 报损单单据分页列表
     */
    Page<LossReportFormPageDto> selectLossReportFormPage(@Param("page") Page<LossReportFormPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<LossReportSearchParam> queryWrapper,
        @Param("lossReportForm") Integer lossReportForm);

    /**
     * 查询单据详情
     *
     * @param busNo 单据号
     * @return 单据详情
     */
    List<ReceiptDetailDto> selectDetail(@Param("busNo") String busNo, @Param("medicine") Integer medicine,
        @Param("device") Integer device, @Param("medicationTableName") String medicationTableName,
        @Param("deviceTableName") String deviceTableName);
}
