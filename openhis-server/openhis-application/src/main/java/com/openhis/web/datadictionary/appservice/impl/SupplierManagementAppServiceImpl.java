/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.core.common.utils.ChineseConvertUtils;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.mapper.SupplierMapper;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.enums.AccountStatus;
import com.openhis.common.enums.SupplierType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.datadictionary.appservice.ISupplierManagementAppService;
import com.openhis.web.datadictionary.dto.SupplierDto;
import com.openhis.web.datadictionary.dto.SupplierInitDto;
import com.openhis.web.datadictionary.dto.SupplierSearchParam;
import com.openhis.web.datadictionary.dto.SupplierUpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 厂商/产地 impl
 *
 * @author dh
 * @date 2025-03-10
 */
@Service
public class SupplierManagementAppServiceImpl implements ISupplierManagementAppService {

    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 厂商/产地初始化
     *
     * @return
     */
    @Override
    public R<?> getSupplierInit() {
        SupplierInitDto supplierInitDto = new SupplierInitDto();
        // 获取厂商/产地种类
        List<SupplierInitDto.supplierTypeOption> supplierTypeOption = Stream.of(SupplierType.values())
            .map(status -> new SupplierInitDto.supplierTypeOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        supplierInitDto.setSupplierTypeOptions(supplierTypeOption);
        return R.ok(supplierInitDto);
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
    @Override
    public R<?> getSupplierList(SupplierSearchParam supplierSearchParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<Supplier> queryWrapper = HisQueryUtils.buildQueryWrapper(supplierSearchParam, searchKey,
            new HashSet<>(Arrays.asList("bus_no", "name", "py_str", "wb_str")), request);
        // 设置排序
        queryWrapper.orderByAsc("bus_no");
        // 分页查询
        Page<SupplierDto> supplierPage =
            HisPageUtils.selectPage(supplierMapper, queryWrapper, pageNo, pageSize, SupplierDto.class);
        // 枚举类回显赋值
        supplierPage.getRecords()
            .forEach(e -> e.setActiveFlag_enumText(EnumUtils.getInfoByValue(AccountStatus.class, e.getActiveFlag())));
        // 返回【病种目录列表DTO】分页
        return R.ok(supplierPage);
    }

    /**
     * 添加供应商信息
     *
     * @param supplierUpDto 供应商信息
     */
    @Override
    public R<?> addSupplyRequest(@Validated @RequestBody SupplierUpDto supplierUpDto) {

        Supplier supplierInfo = new Supplier();
        BeanUtils.copyProperties(supplierUpDto, supplierInfo);
        // 设置拼音首拼
        supplierInfo.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(supplierInfo.getName()));
        // 设置五笔首拼
        supplierInfo.setWbStr(ChineseConvertUtils.toWBFirstLetter(supplierInfo.getName()));
        return supplierService.addSupplier(supplierInfo)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"厂商/供应商信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
    }

    /**
     * 编辑供应商信息
     *
     * @param supplierUpDto 供应商信息
     */
    @Override
    public R<?> editSupplyRequest(@Validated @RequestBody SupplierUpDto supplierUpDto) {

        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierUpDto, supplier);
        // 设置拼音首拼
        supplier.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(supplier.getName()));
        // 设置五笔首拼
        supplier.setWbStr(ChineseConvertUtils.toWBFirstLetter(supplier.getName()));
        // 更新供应商信息信息
        return supplierService.updateById(supplier)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"厂商/供应商信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 厂商/产地详细查询
     *
     * @param id 查询条件
     * @return 厂商/产地查询结果
     */
    @Override
    public R<?> getSupplierDetail(@PathVariable("id") Long id) {
        SupplierDto supplierDto = new SupplierDto();
        // 根据ID查询【供应商信息】
        Supplier supplier = supplierService.getById(id);
        BeanUtils.copyProperties(supplier, supplierDto);
        return R.ok(supplierDto);
    }

    /**
     * 厂商/产地停用
     *
     * @param ids 厂商/产地ID列表
     * @return
     */
    @Override
    public R<?> editSupplierStop(List<Long> ids) {
        List<Supplier> supplierList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            Supplier supplier = new Supplier();
            supplier.setId(detail);
            supplier.setActiveFlag(0);
            supplierList.add(supplier);
        }
        // 更新厂商/产地信息
        return supplierService.updateBatchById(supplierList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 厂商/产地启用
     *
     * @param ids 厂商/产地ID列表
     * @return
     */
    @Override
    public R<?> editSupplierStart(List<Long> ids) {
        List<Supplier> supplierListStart = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            Supplier supplier = new Supplier();
            supplier.setId(detail);
            supplier.setActiveFlag(1);
            supplierListStart.add(supplier);
        }
        // 更新厂商/产地信息
        return supplierService.updateBatchById(supplierListStart)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }
}
