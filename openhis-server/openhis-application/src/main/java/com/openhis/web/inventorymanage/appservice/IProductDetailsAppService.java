package com.openhis.web.inventorymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.ProductDetailsSearchParam;

/**
 * 库存商品明细 appService
 *
 * @author yuanzs
 * @date 2025-04-24
 */
public interface IProductDetailsAppService {

    /**
     * 库存商品明细初始化
     *
     * @return 下拉列表值
     */
    R<?> getInit();

    /**
     * 查询库存商品明细分页列表
     *
     * @param productDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 库存商品明细分页列表
     */
    R<?> getPage(ProductDetailsSearchParam productDetailsSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);

    /**
     * 操作：停供
     *
     * @param id 库存项目管理ID
     * @return 操作结果
     */
    R<?> stopSupplyById(Long id);

    /**
     * 操作：取消停供
     *
     * @param id 库存项目管理ID
     * @return 操作结果
     */
    R<?> cancelSupplyById(Long id);

}
