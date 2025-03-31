/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.ProductTransferDto;
import com.openhis.web.inventorymanage.dto.SupplySearchParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品调拨 appService
 *
 * @author zwh
 * @date 2025-03-08
 */
public interface IProductTransferAppService {

    /**
     * 商品调拨页面初始化
     *
     * @return 初始化信息
     */
    R<?> productTransferInit();

    /**
     * 商品调拨单据列表
     *
     * @param supplySearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨单据分页列表
     */
    R<?> getPage(SupplySearchParam supplySearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request);

    /**
     * 商品调拨单据详情
     *
     * @param busNo 单据号
     * @return 入库单据详情
     */
    R<?> getDetail(String busNo);

    /**
     * 添加/编辑商品调拨单据
     *
     * @param productTransferDto 入库单据
     * @return 编辑结果
     */
    R<?> addOrEditTransferReceipt(ProductTransferDto productTransferDto);

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
