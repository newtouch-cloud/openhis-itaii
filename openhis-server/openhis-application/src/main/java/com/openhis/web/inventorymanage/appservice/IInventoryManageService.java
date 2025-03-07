/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import java.util.List;

import com.core.common.core.domain.R;
import com.openhis.administration.domain.ChargeItemDefApp;
import com.openhis.web.inventorymanage.dto.ItemChargeDetailDto;
import com.openhis.web.inventorymanage.dto.SupplyItemDetailDto;
import com.openhis.workflow.domain.SupplyRequest;

/**
 * 采购入库 service
 *
 * @author zwh
 * @date 2025-03-05
 */
public interface IInventoryManageService {

    /**
     * 校验单据是否正确
     *
     * @param supplyRequest 单据信息
     * @return 校验结果
     */
    R<?> verifyInventoryReceipt(SupplyRequest supplyRequest);

    /**
     * 根据单据号获取供应单据及供应项相关详细信息
     *
     * @param busNo 单据号
     * @param itemTable 供应项所在表名
     * @return 供应单据及供应项相关详细信息
     */
    List<SupplyItemDetailDto> getSupplyItemDetail(String busNo, String itemTable);

    /**
     * 获取物品的价格信息
     *
     * @param itemIdList 物品id
     * @return 价格信息
     */
    List<ItemChargeDetailDto> getItemChargeDetail(List<Long> itemIdList);

    /**
     * 入库项价格验证
     *
     * @param agreedList 供应单据
     * @param chargeDetailList 项目价格
     * @return 价格定义子表数据
     */
    List<ChargeItemDefApp> verifyItemCharge(List<SupplyRequest> agreedList, List<ItemChargeDetailDto> chargeDetailList);
}
