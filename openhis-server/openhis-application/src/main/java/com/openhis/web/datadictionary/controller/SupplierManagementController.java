/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.datadictionary.dto.SupplierDto;
import com.openhis.web.datadictionary.dto.SupplierSearchParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.service.IConditionDefinitionService;

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
@RequestMapping("/data-dictionary-supplier")
@Slf4j
@AllArgsConstructor
public class SupplierManagementController {
    private final ISupplierService supplierService;

    /**
     * 厂商/产地查询
     *
     * @param supplierSearchParam 查询条件
     * @param pageNo 查询条件
     * @param pageSize 查询条件
     * @return 厂商/产地查询结果
     */
    @GetMapping(value = "/get-supplier-list")
    public R<?> getSupplierList(SupplierSearchParam supplierSearchParam,
                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 查询条件初始化
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierSearchParam, supplier);

        // 查询
        Page<Supplier> supplierPage = supplierService.getPage(supplier,pageNo,pageSize);
        // 定义【入库单据分页列表DTO】的分页，传入【页码】、【行数】、及上面分页的【总数】
        Page<SupplierDto> returnPage =
                new Page<>(supplierPage.getCurrent(), supplierPage.getSize(), supplierPage.getTotal());
//        supplierPage
//        supplierPage.map(item -> {
//            SupplierDto supplierDto = new SupplierDto();
//            BeanUtils.copyProperties(item, supplierDto); // 使用 BeanUtils 复制属性
//            // 如果有特殊字段需要手动转换，可以在这里处理
//            return supplierDto;
//        });

        return R.ok(returnPage);
    }

    /**
     * 添加供应商信息
     *
     * @param supplierDto 供应商信息
     */
    @PostMapping("/add-supplier")
    public R<?> addSupplyRequest(@Validated @RequestBody SupplierDto supplierDto) {
        // 初始化参数
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierDto, supplier);
        // 如果业务上不需要其它处理 直接调用service的保存方法
        boolean saveSupplierSuccess = supplierService.save(supplier);

        if (!saveSupplierSuccess) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }

        return saveSupplierSuccess
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"厂商/供应商信息"}))
                : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 编辑供应商信息
     *
     * @param supplierDto 供应商信息
     */
    @PutMapping("/edit-supplier")
    public R<?> editSupplyRequest(@Validated @RequestBody  SupplierDto supplierDto) {
        // 初始化参数
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierDto, supplier);

        return supplierService.updateById(supplier) ?
                R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"厂商/供应商信息"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }


    /**
     * 厂商/产地详细查询
     *
     * @param supplierId 查询条件
     * @return 厂商/产地查询结果
     */
    @GetMapping(value = "/get-supplier-detail")
    public R<?> getSupplierDetail(@RequestParam(name = "id", required = true) String supplierId) {


        // 查询
        Supplier supplierDetail = supplierService.getById(supplierId);
        return R.ok(supplierDetail);
    }
}
