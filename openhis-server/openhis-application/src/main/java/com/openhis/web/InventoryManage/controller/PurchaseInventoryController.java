/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventoryManage.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.service.IPatientService;
import com.openhis.medication.domain.Medication;
import com.openhis.medication.service.IMedicationService;
import com.openhis.web.inventoryManage.assembler.PurchaseInventoryAssembler;
import com.openhis.web.inventoryManage.dto.SupplySearchParam;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.ISupplyRequestService;

import lombok.extern.slf4j.Slf4j;

/**
 * 采购入库 controller
 *
 * @author zwh
 * @date 2025-02-18
 */
@RestController
@RequestMapping("/inventory-manage-purchase")
@Slf4j
public class PurchaseInventoryController {
    @Autowired
    private ISupplyRequestService supplyRequestService;
    @Autowired
    private IMedicationService medicationService;
    @Autowired
    private IPatientService patientService;

    /**
     * 入库单据分页列表
     *
     * @param supplySearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 入库单据分页列表
     */
    @GetMapping(value = "/inventory-receipt-page")
    public Page<SupplyRequest> getPage(SupplySearchParam supplySearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        // 查询supply_request相关信息并返回分页列表

        return supplyRequestService.page(new Page<>(pageNo,pageSize));
    }

    // 添加入库单据之前需要
    // 1.supplier供应商信息列表
    // 2.location信息列表包括（药房，药库，材料柜，护理站）
    // 3.practitioner_role与practitioner联查获取对应location的管理员列表
    // 4.查询选定对应药品类型的药品信息列表

    /**
     * 添加入库单据（生成供应请求）
     *
     * @param supplyRequest 供应请求信息
     */
    @PostMapping("/add-supply-request")
    public void addSupplyRequest(@Validated @RequestBody SupplyRequest supplyRequest) {
        // 生成待发送的入库单据supply_request
        // 生成收费项目charge_item

        // 如果采购单价被修改了，需要根据批次号更新采购单价子表价格
    }

    /**
     * 编辑入库单据
     *
     * @param supplyRequest 供应请求信息
     */
    @PutMapping("/edit-supply-request")
    public void editSupplyRequest(@Validated @RequestBody SupplyRequest supplyRequest) {
        // 更新supply_request信息
        // 更新收费项目charge_item
    }

    /**
     * 单据提交申请
     *
     * @param supplyRequest 供应请求信息
     */
    @PutMapping("/submit-examine")
    public void submitExamine(SupplyRequest supplyRequest) {

        // 更改供应请求单据状态
        // 生成供应分发supply_delivery
    }

    /**
     * 入库单据详情列表
     *
     * @param supplySearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 入库单据分页列表
     */
    @GetMapping(value = "/inventory-receipt-page")
    public R<?> getDetailPage(SupplySearchParam supplySearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 数据初始化
        Medication medication = new Medication();
        BeanUtils.copyProperties(supplySearchParam, medication);

        SupplyRequest supplyRequest = new SupplyRequest();
        BeanUtils.copyProperties(supplySearchParam, supplyRequest);

        // 获取供应请求信息

        // ====================================================================================

        // 查询【供应申请管理】分页列表
        Page<SupplyRequest> supplyRequestPage = supplyRequestService.getPage(supplyRequest, pageNo, pageSize);

        // 根据【发放id】查询【药品基本信息管理】列表
        List<Medication> medicationList = medicationService.listByIds(
            supplyRequestPage.getRecords().stream().map(SupplyRequest::getDispenseId).collect(Collectors.toList()));

        // 根据【患者id】查询【患者管理】列表
        List<Patient> patientList = patientService.listByIds(
            supplyRequestPage.getRecords().stream().map(SupplyRequest::getPatientId).collect(Collectors.toList()));

        // 装配并返回【入库单据分页列表DTO】分页
        return R.ok(
            PurchaseInventoryAssembler.assembleInventoryReceiptDto(supplyRequestPage, medicationList, patientList),
            "查询成功");

    }
}
