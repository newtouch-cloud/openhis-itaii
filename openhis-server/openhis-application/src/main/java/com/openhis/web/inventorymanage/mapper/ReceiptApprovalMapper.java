/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.inventorymanage.dto.ItemChargeDetailDto;
import com.openhis.web.inventorymanage.dto.ReceiptApprovalSearchParam;
import com.openhis.web.inventorymanage.dto.ReceiptPageDto;
import com.openhis.web.inventorymanage.dto.SupplyItemDetailDto;
import com.openhis.workflow.domain.SupplyRequest;

/**
 * 单据审批查询用 mapper
 *
 * @author zwh
 * @date 2025-02-25
 */
@Repository
public interface ReceiptApprovalMapper extends BaseMapper<SupplyRequest> {

    /**
     * 获取供应项目详细信息
     *
     * @param busNo 单据号
     * @param completed 发放状态：已完成
     * @return 供应项目详细信息
     */
    List<SupplyItemDetailDto> selectSupplyDetail(@Param("busNo") String busNo, @Param("completed") Integer completed);

    /**
     * 根据物品id获取物品的价格信息
     *
     * @param itemIdList 物品id
     * @return 价格信息
     */
    List<ItemChargeDetailDto> selectChargeDetail(@Param("itemIdList") List<Long> itemIdList);

    /**
     * 查询单据审批分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param approval 单据状态：审核中
     * @param agree 单据状态：同意
     * @param reject 单据状态：驳回
     * @return 单据审批分页列表
     */
    Page<ReceiptPageDto> selectReceiptPage(@Param("page") Page<ReceiptPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<ReceiptApprovalSearchParam> queryWrapper,
        @Param("approval") Integer approval, @Param("agree") Integer agree, @Param("reject") Integer reject);
}
