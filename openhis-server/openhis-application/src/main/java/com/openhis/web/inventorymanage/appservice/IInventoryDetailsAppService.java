/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.InventoryDetailsSearchParam;
import com.openhis.web.inventorymanage.dto.InventoryStockTakeDetailDto;
import com.openhis.web.inventorymanage.dto.RequisitionOutSearchParam;

/**
 * 库存相关明细查询 service
 *
 * @author
 * @date 2025-03-10
 */
public interface IInventoryDetailsAppService {

    /**
     * 采购入库明细查询
     *
     * @param inventoryDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 采购入库明细查询分页列表
     */
    R<?> purchaseInGetPage(InventoryDetailsSearchParam inventoryDetailsSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);

    /**
     * 商品调拨查询
     *
     * @param inventoryDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨查询分页列表
     */
    R<?> transferGetPage(InventoryDetailsSearchParam inventoryDetailsSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);

    /**
     * 领用出库查询
     *
     * @param inventoryDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 领用出库查询分页列表
     */
    R<?> requisitionOutGetPage(InventoryDetailsSearchParam inventoryDetailsSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request);

    /**
     * 商品盘点查询
     *
     * @param inventoryDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品盘点查询分页列表
     */
    R<?> inventoryStockGetPage(InventoryDetailsSearchParam inventoryDetailsSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request);

}
