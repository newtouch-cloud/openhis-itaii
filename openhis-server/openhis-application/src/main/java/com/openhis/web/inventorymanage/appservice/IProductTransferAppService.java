/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.BatchTransferSearchParam;
import com.openhis.web.inventorymanage.dto.ProductTransferDto;
import com.openhis.web.inventorymanage.dto.SupplySearchParam;

import java.util.List;

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
     * 商品调拨单据编号初始化
     *
     * @return 初始化信息
     */
    R<?> productTransferNoInit();

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
     * 生成商品批量调拨单据
     *
     * @param batchTransferSearchParam 生成批量调拨单查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 商品批量调拨单据
     */
    R<?> createBatchTransfer(BatchTransferSearchParam batchTransferSearchParam, Integer pageNo, Integer pageSize,
        HttpServletRequest request);

    /**
     * 保存商品批量调拨单据
     *
     * @param productTransferDtoList 商品批量调拨单据
     * @return 操作结果
     */
    R<?> addOrEditBatchTransferReceipt(List<ProductTransferDto> productTransferDtoList, Boolean flag);

    /**
     * 商品调拨单据详情
     *
     * @param busNo 单据号
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 入库单据详情
     */
    R<?> getDetail(String busNo, Integer pageNo, Integer pageSize);

    /**
     * 添加/编辑商品调拨单据(批量)
     *
     * @param productTransferDtoList 入库单据
     * @return 编辑结果
     */
    R<?> addOrEditTransferReceipt(List<ProductTransferDto> productTransferDtoList);

    /**
     * 删除单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    R<?> deleteReceipt(List<Long> supplyRequestIds);

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
