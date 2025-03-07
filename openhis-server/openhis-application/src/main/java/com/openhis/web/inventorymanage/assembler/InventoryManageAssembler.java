/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.assembler;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.openhis.administration.domain.ChargeItem;
import com.openhis.web.inventorymanage.dto.SupplyItemDetailDto;
import com.openhis.workflow.domain.InventoryItem;

/**
 * 入库管理dto转换器
 *
 * @author zwh
 * @date 2025-03-05
 */
public class InventoryManageAssembler {

    /**
     * 将供应项目的详细信息装配为库存项目和采购账单
     * 
     * @param supplyItemDetailList 供应项目的详细信息
     * @param now 当前时间
     * @return 库存项目和采购账单
     */
    public static Pair<List<ChargeItem>, List<InventoryItem>>
        assembleChargeAndInventory(List<SupplyItemDetailDto> supplyItemDetailList, Date now) {
        return null;
    }
}
