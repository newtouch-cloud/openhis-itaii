/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.ProductStocktakingDto;
import com.openhis.web.inventorymanage.dto.ProductStocktakingSearchParam;
import com.openhis.web.inventorymanage.dto.ReceiptDetailDto;
import com.openhis.web.inventorymanage.dto.StocktakingBatchSearchParam;

import java.util.List;

/**
 * 商品盘点 appService
 *
 * @author zwh
 * @date 2025-03-11
 */
public interface IProductStocktakingAppService {

    /**
     * 商品盘点页面初始化
     *
     * @return 初始化信息
     */
    R<?> productStocktakingInit();

    /**
     * 获取单据号
     *
     * @return 初始化信息
     */
    R<?> productStocktakingBusNoInit();

    /**
     * 商品盘点列表
     *
     * @param stocktakingSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 盘点单据分页列表
     */
    R<?> getPage(ProductStocktakingSearchParam stocktakingSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);

    /**
     * 盘点单据详情
     *
     * @param busNo 单据号
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 盘点单据详情
     */
    R<?> getDetail(String busNo, Integer pageNo, Integer pageSize);

    /**
     * 添加/编辑商品盘点(批量)
     *
     * @param productStocktakingDtoList 盘点信息
     * @return 操作结果
     */
    R<?> addOrEditProductStocktaking(List<ProductStocktakingDto> productStocktakingDtoList);

    /**
     * 生成批量盘点单
     *
     * @param stocktakingSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 盘点单据分页列表
     */
    R<?> getBatchDetail(StocktakingBatchSearchParam stocktakingSearchParam, Integer pageNo, Integer pageSize,
                        String searchKey, HttpServletRequest request);

    /**
     * 保存批量盘点单
     *
     * @param productStocktakingDtoList 盘点单据
     * @return 执行结果
     */
    R<?> addBatchDetail(List<ProductStocktakingDto> productStocktakingDtoList);

    /**
     * 删除盘点
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
