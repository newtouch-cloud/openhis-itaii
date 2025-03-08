/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.ItemChargeDetailDto;
import com.openhis.web.inventorymanage.dto.SupplyItemDetailDto;
import com.openhis.workflow.domain.SupplyRequest;

/**
 * 单据审批 service
 *
 * @author zwh
 * @date 2025-03-05
 */
public interface IReceiptApprovalAppService {

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
     * 审批通过
     *
     * @param busNo 单据号
     * @param request 请求数据
     * @return 操作结果
     */
    R<?> approved(String busNo, HttpServletRequest request);

    /**
     * 审批驳回
     *
     * @param busNo 单据号
     * @param request 请求数据
     * @return 操作结果
     */
    R<?> reject(String busNo, HttpServletRequest request);
}
