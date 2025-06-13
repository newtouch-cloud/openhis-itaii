/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import com.openhis.web.reportmanage.dto.ReturnIssueReportPageDto;
import com.openhis.web.reportmanage.dto.ReturnIssueReportSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 退库明细查询用 mapper
 *
 * @author ym
 * @date 2025-05-23
 */
@Repository
public interface ReturnIssueReportMapper {

    /**
     * 出库明细查询
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @param returnissue 单据类型：领用退库
     * @param supplyStatus 单据状态：同意
     * @param deliveryStatus 发放状态：已完成
     * @return 出库明细
     */
    Page<ReturnIssueReportPageDto> selectReturnIssueReportPage(@Param("page") Page<ReturnIssueReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<ReturnIssueReportSearchParam> queryWrapper,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("returnissue") Integer returnissue, @Param("supplyStatus") Integer supplyStatus,
        @Param("deliveryStatus") Integer deliveryStatus);
}
