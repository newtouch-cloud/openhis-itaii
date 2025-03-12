/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.InventoryReceiptDto;
import com.openhis.web.inventorymanage.dto.InventorySearchParam;

/**
 * 采购入库 appService
 *
 * @author zwh
 * @date 2025-03-08
 */
public interface IPurchaseInventoryAppService {

    /**
     * 入库单据列表
     *
     * @param inventorySearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 入库单据分页列表
     */
    R<?> getPage(InventorySearchParam inventorySearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request);

    /**
     * 入库单据详情
     *
     * @param busNo 单据号
     * @return 入库单据详情
     */
    R<?> getDetail(String busNo);

    /**
     * 添加/编辑入库单据
     *
     * @param inventoryReceiptDto 入库单据
     * @return 操作结果
     */
    R<?> addOrEditInventoryReceipt(InventoryReceiptDto inventoryReceiptDto);

    /**
     * 删除单据
     *
     * @param supplyRequestId 供应请求id
     * @return 操作结果
     */
    R<?> deleteReceipt(Long supplyRequestId);

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    R<?> submitApproval(String busNo);

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    R<?> withdrawApproval(String busNo);
}
