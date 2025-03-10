/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.web.inventorymanage.dto.ItemChargeDetailDto;
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
     * 获取药品供应单据详细信息
     *
     * @param busNo 单据号
     * @param completed 发放状态：已完成
     * @return 药品供应单据详细信息
     */
    List<SupplyItemDetailDto> selectSupplyMedDetail(@Param("busNo") String busNo,
        @Param("completed") Integer completed);

    /**
     * 获取耗材供应单据详细信息
     *
     * @param busNo 单据号
     * @param completed 发放状态：已完成
     * @return 耗材供应单据详细信息
     */
    List<SupplyItemDetailDto> selectSupplyDevDetail(@Param("busNo") String busNo,
        @Param("completed") Integer completed);

    /**
     * 根据物品id获取物品的价格信息
     *
     * @param itemIdList 物品id
     * @return 价格信息
     */
    List<ItemChargeDetailDto> selectChargeDetail(@Param("itemIdList") List<Long> itemIdList);
}
