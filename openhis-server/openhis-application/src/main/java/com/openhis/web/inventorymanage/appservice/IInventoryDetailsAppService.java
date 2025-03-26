/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.openhis.web.inventorymanage.dto.*;

/**
 * 采购入库明细查询 service
 *
 * @author
 * @date 2025-03-10
 */
public interface IInventoryDetailsAppService {

    /**
     * 采购入库查询
     *
     * @param purchaseInSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 采购入库查询分页列表
     */
    IPage<PurchaseInDetailDto> purchaseInGetPage(PurchaseInSearchParam purchaseInSearchParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 领用出库查询
     *
     * @param requisitionOutSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 领用出库查询分页列表
     */
    IPage<RequisitionOutDetailDto> requisitionOutGetPage(RequisitionOutSearchParam requisitionOutSearchParam,
        String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 商品调拨查询
     *
     * @param requisitionOutSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨查询分页列表
     */
    IPage<InventoryTransferDetailDto> inventoryTransferGetPage(RequisitionOutSearchParam requisitionOutSearchParam,
        String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 商品盘点查询
     *
     * @param requisitionOutSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品盘点查询分页列表
     */
    IPage<InventoryStockTakeDetailDto> inventoryStockTakeGetPage(RequisitionOutSearchParam requisitionOutSearchParam,
        String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request);

}
