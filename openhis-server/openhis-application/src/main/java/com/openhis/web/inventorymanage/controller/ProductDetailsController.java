package com.openhis.web.inventorymanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.appservice.IProductDetailsAppService;
import com.openhis.web.inventorymanage.dto.ProductDetailsSearchParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 库存商品明细 controller
 *
 * @author yuanzs
 * @date 2025-04-24
 */
@RestController
@RequestMapping("/inventory-manage/product")
@Slf4j
public class ProductDetailsController {

    @Autowired
    private IProductDetailsAppService productDetailsAppService;

    /**
     * 库存商品明细初始化
     *
     * @return 下拉列表值
     */
    @GetMapping("/product-init")
    public R<?> getMedicationInit() {
        return productDetailsAppService.getInit();
    }

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
    @GetMapping(value = "/product-page")
    public R<?> getPage(ProductDetailsSearchParam productDetailsSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return productDetailsAppService.getPage(productDetailsSearchParam, pageNo, pageSize, searchKey, request);
    }

    /**
     * 操作：停供
     *
     * @param id 库存项目管理ID
     * @return 操作结果
     */
    @PutMapping("/stop-supply")
    public R<?> stopSupplyById(@RequestBody Long id) {
        return productDetailsAppService.stopSupplyById(id);
    }

    /**
     * 操作：取消停供
     *
     * @param id 库存项目管理ID
     * @return 操作结果
     */
    @PutMapping("/cancel-supply")
    public R<?> cancelSupplyById(@RequestBody Long id) {
        return productDetailsAppService.cancelSupplyById(id);
    }

}
