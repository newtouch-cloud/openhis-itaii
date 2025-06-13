/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.common.dto.InventoryItemDto;
import com.openhis.web.common.dto.InventoryItemParam;
import com.openhis.web.common.dto.LocationInventoryDto;

/**
 * app常用接口 mapper
 *
 * @author zwh
 * @date 2025-04-01
 */
@Repository
public interface CommonAppMapper {

    /**
     * 查询库存项目信息
     *
     * @param page 分页
     * @param medicationTableName 药品表名
     * @param deviceTableName 耗材表名
     * @param purchaseFlag 是否入库
     * @param purchase 命中条件:采购
     * @param retired 停用
     * @param queryWrapper 查询条件
     * @return 库存项目信息
     */
    IPage<InventoryItemDto> selectInventoryItemList(@Param("page") Page<InventoryItemDto> page,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("medicine") Integer medicine, @Param("device") Integer device,
        @Param("purchaseFlag") Integer purchaseFlag, @Param("purchase") String purchase,
        @Param("retired") Integer retired, @Param(Constants.WRAPPER) QueryWrapper<InventoryItemParam> queryWrapper);

    /**
     * 查询项目库存相关信息
     *
     * @param itemId 项目id
     * @param lotNumber 批号
     * @param orgLocationId 源仓库
     * @param objLocationId 目的仓库
     * @param purchase 价格命中条件：采购
     * @return 项目库存相关信息
     */
    List<LocationInventoryDto> selectInventoryItemInfo(@Param("orgLocationId") Long orgLocationId,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("objLocationId") Long objLocationId, @Param("lotNumber") String lotNumber, @Param("itemId") Long itemId,
        @Param("purchase") String purchase);
}
