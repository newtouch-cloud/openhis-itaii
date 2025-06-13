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
import com.openhis.web.common.dto.LocationInventoryDto;
import com.openhis.web.inventorymanage.dto.InventorySearchParam;
import com.openhis.web.inventorymanage.dto.ReceiptDetailDto;
import com.openhis.web.inventorymanage.dto.ReceiptPageDto;

/**
 * 采购入库查询用 mapper
 *
 * @author zwh
 * @date 2025-03-10
 */
@Repository
public interface PurchaseInventoryMapper {

    /**
     * 查询入库单据分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param purchaseInventory 单据类型：采购入库
     * @return 入库单据分页列表
     */
    Page<ReceiptPageDto> selectInventoryReceiptPage(@Param("page") Page<ReceiptPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<InventorySearchParam> queryWrapper,
        @Param("purchaseInventory") Integer purchaseInventory);

    /**
     * 查询单据详情
     *
     * @param busNo 单据号
     * @return 单据详情
     */
    List<ReceiptDetailDto> selectDetail(@Param("busNo") String busNo);
}
