/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.common.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.common.appservice.ICommonService;
import com.openhis.web.common.dto.InventoryItemParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * app常用接口
 *
 * @author zwh
 * @date 2025-04-01
 */
@RestController
@RequestMapping("/app-common")
@Slf4j
@AllArgsConstructor
public class CommonAppController {

    @Resource
    private ICommonService commonService;

    /**
     * 药房列表
     *
     * @return 药房列表
     */
    @GetMapping(value = "/pharmacy-list")
    public R<?> getPharmacyList() {
        return R.ok(commonService.getPharmacyList());
    }

    /**
     * 药房列表(库房用)
     *
     * @return 药房列表
     */
    @GetMapping(value = "/inventory-pharmacy-list")
    public R<?> getInventoryPharmacyList() {
        return R.ok(commonService.getInventoryPharmacyList());
    }

    /**
     * 药库列表
     *
     * @return 药库列表
     */
    @GetMapping(value = "/cabinet-list")
    public R<?> getCabinetList() {
        return R.ok(commonService.getCabinetList());
    }

    /**
     * 药库列表(库房用)
     *
     * @return 药库列表
     */
    @GetMapping(value = "/inventory-cabinet-list")
    public R<?> getInventoryCabinetList() {
        return R.ok(commonService.getInventoryCabinetList());
    }

    /**
     * 药房药库列表
     *
     * @return 药房药库列表
     */
    @GetMapping(value = "/pharmacy-cabinet-list")
    public R<?> getPharmacyCabinetList() {
        return R.ok(commonService.getPharmacyCabinetList());
    }

    /**
     * 病区列表
     *
     * @return 病区列表
     */
    @GetMapping(value = "/ward-list")
    public R<?> getWardList() {
        return R.ok(commonService.getWardList());
    }

    /**
     * 科室列表
     *
     * @return 科室列表
     */
    @GetMapping(value = "/department-list")
    public R<?> getDepartmentList() {
        return commonService.getDepartmentList();
    }

    /**
     * 库存项目下拉列表（药库业务使用）
     *
     * @param inventoryItemParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 库存项目信息
     */
    @GetMapping(value = "/inventory-item")
    public R<?> getInventoryItemList(InventoryItemParam inventoryItemParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize) {
        return commonService.getInventoryItemList(inventoryItemParam, searchKey, pageNo, pageSize);
    }

    /**
     * 根据项目相关信息查询项目库存相关信息
     *
     * @param inventoryItemParam 项目id
     * @return 项目库存相关信息
     */
    @GetMapping(value = "/inventory-item-info")
    public R<?> getInventoryItemInfo(InventoryItemParam inventoryItemParam) {
        return commonService.getInventoryItemInfo(inventoryItemParam);
    }

    /**
     * 根据追溯码获取药品/耗材信息
     *
     * @param traceNoList 追溯码列表
     * @return 项目信息
     */
    @GetMapping(value = "/item-trace-no")
    public R<?> getItemInfoByTraceNo(@RequestParam List<String> traceNoList) {
        return commonService.getItemInfoByTraceNo(traceNoList);
    }

}
