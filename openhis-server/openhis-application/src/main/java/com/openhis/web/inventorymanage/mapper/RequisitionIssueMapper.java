/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import java.util.List;

import com.openhis.web.inventorymanage.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 领用出库查询用 mapper
 *
 * @author CY
 * @date 2025-04-03
 */
@Repository
public interface RequisitionIssueMapper {
    /**
     * 查询领用部门列表
     *
     * @return 领用部门列表
     */
    List<IssueDepartmentDto> selectRequisitionDepartment(@Param("department") Integer department);

    /**
     * 查询领用出库单据分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param issueInventory 单据类型：领用出库
     * @return 领用出库单据分页列表
     */
    Page<IssuePageDto> selectRequisitionIssuePage(@Param("page") Page<IssuePageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<IssueSearchParam> queryWrapper,
        @Param("issueInventory") Integer issueInventory);

    /**
     * 查询单据详情
     *
     * @param busNo 单据号
     * @return 单据详情
     */
    List<IssueDetailDto> requisitionIssueDetail(@Param("busNo") String busNo,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("medicine") Integer medicine, @Param("device") Integer device);
}
