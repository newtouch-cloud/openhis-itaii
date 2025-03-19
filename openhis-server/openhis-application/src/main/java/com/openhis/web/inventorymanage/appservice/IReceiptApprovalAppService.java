/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.ItemChargeDetailDto;
import com.openhis.web.inventorymanage.dto.ReceiptApprovalSearchParam;
import com.openhis.web.inventorymanage.dto.SupplyItemDetailDto;

/**
 * 单据审批 appService
 *
 * @author zwh
 * @date 2025-03-05
 */
public interface IReceiptApprovalAppService {

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
     * 入库单据审批通过
     *
     * @param busNo 单据号
     * @param request 请求数据
     * @return 操作结果
     */
    R<?> purchaseInventoryApproved(String busNo, HttpServletRequest request);

    /**
     * 审批驳回
     *
     * @param busNo 单据号
     * @param request 请求数据
     * @return 操作结果
     */
    R<?> reject(String busNo, HttpServletRequest request);

    /**
     * 审批单据分页列表
     *
     * @param receiptSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 审批单据分页列表
     */
    R<?> getPage(ReceiptApprovalSearchParam receiptSearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request);

    /**
     * 单据审批页面初始化
     *
     * @return 初始化信息
     */
    R<?> receiptApprovalInit();
}
