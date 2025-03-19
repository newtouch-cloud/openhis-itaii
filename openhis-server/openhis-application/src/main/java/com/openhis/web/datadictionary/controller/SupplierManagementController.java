/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.controller;

import java.util.List;
import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.appservice.ISupplierManagementAppService;
import com.openhis.web.datadictionary.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO:厂商/产地
 *
 * @author dh
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/data-dictionary/supplier")
@Slf4j
@AllArgsConstructor
public class SupplierManagementController {

    @Autowired
    private ISupplierManagementAppService supplierManagementAppService;

    /**
     * 厂商/产地初始化
     *
     * @return
     */
    @GetMapping("/information-init")
    public R<?> getSupplierInit() {
        return supplierManagementAppService.getSupplierInit();
    }

    /**
     * 厂商/产地查询
     *
     * @param supplierSearchParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 查询条件
     * @param pageSize 查询条件
     * @return 厂商/产地查询结果
     */
    @GetMapping(value = "/get-supplier-list")
    public R<?> getSupplierList(SupplierSearchParam supplierSearchParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 返回分页
        return  supplierManagementAppService.getSupplierList(supplierSearchParam, searchKey, pageNo, pageSize, request);
    }

    /**
     * 添加供应商信息
     *
     * @param supplierUpDto 供应商信息
     */
    @PostMapping("/add-supplier")
    public R<?> addSupplyRequest(@Validated @RequestBody SupplierUpDto supplierUpDto) {
        return supplierManagementAppService.addSupplyRequest(supplierUpDto);
    }

    /**
     * 编辑供应商信息
     *
     * @param supplierUpDto 供应商信息
     */
    @PutMapping("/edit-supplier")
    public R<?> editSupplyRequest(@Validated @RequestBody SupplierUpDto supplierUpDto) {

        // 更新供应商信息信息
        return supplierManagementAppService.addSupplyRequest(supplierUpDto);
    }

    /**
     * 厂商/产地详细查询
     *
     * @param id 查询条件
     * @return 厂商/产地查询结果
     */
    @GetMapping(value = "/get-supplier-detail")
    public R<?> getSupplierDetail(@RequestParam Long id) {
        return supplierManagementAppService.getSupplierDetail(id);
    }

    /**
     * 厂商/产地停用
     *
     * @param ids 厂商/产地ID列表
     * @return
     */
    @PutMapping("/information-stop")
    public R<?> editSupplierStop(@RequestBody List<Long> ids) {
        // 更新厂商/产地信息
        return supplierManagementAppService.editSupplierStop(ids);
    }

    /**
     * 厂商/产地启用
     *
     * @param ids 厂商/产地ID列表
     * @return
     */
    @PutMapping("/information-start")
    public R<?> editSupplierStart(@RequestBody List<Long> ids) {
        // 更新厂商/产地信息
        return supplierManagementAppService.editSupplierStart(ids);
    }
}
