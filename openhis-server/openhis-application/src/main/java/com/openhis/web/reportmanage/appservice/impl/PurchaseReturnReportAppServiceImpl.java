/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.openhis.web.reportmanage.appservice.PurchaseReturnReportAppService;
import com.openhis.web.reportmanage.dto.*;
import com.openhis.web.reportmanage.mapper.PurchaseReturnReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.EventStatus;
import com.openhis.common.enums.SupplyStatus;
import com.openhis.common.enums.SupplyType;
import com.openhis.common.utils.HisQueryUtils;

/**
 * 采购退货明细报表 impl
 *
 * @author ym
 * @date 2025-05-23
 */
@Service
public class PurchaseReturnReportAppServiceImpl implements PurchaseReturnReportAppService {

    @Autowired
    private PurchaseReturnReportMapper purchaseReturnReportMapper;

    @Resource
    private ISupplierService supplierService;

    /**
     * 采购退货明细页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {

        PurchaseReturnReportInitDto initDto = new PurchaseReturnReportInitDto();
        // 查询供应商列表
        List<Supplier> supplierList = supplierService.getList();

        // 供应商信息
        List<PurchaseReturnReportInitDto.supplierListOption> supplierListOptions = supplierList.stream()
                .map(supplier -> new PurchaseReturnReportInitDto.supplierListOption(supplier.getId(), supplier.getName()))
                .collect(Collectors.toList());

        initDto.setSupplierListOptions(supplierListOptions);

        return R.ok(initDto);
    }

    /**
     * 采购退货明细列表
     *
     * @param purchaseReturnReportSearchParam 采购退货明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 采购退货明细
     */
    @Override
    public R<?> getPage(PurchaseReturnReportSearchParam purchaseReturnReportSearchParam, Integer pageNo, Integer pageSize,
                        String searchKey, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<PurchaseReturnReportSearchParam> queryWrapper = HisQueryUtils.buildQueryWrapper(purchaseReturnReportSearchParam,
            searchKey, new HashSet<>(Arrays.asList("supply_bus_no", "name", "bus_no")), request);

        // 查询采购退货明细分页列表
        Page<PurchaseReturnReportPageDto> purchaseReturnReportPageDto = purchaseReturnReportMapper.selectPurchaseReturnReportPage(
            new Page<>(pageNo, pageSize), queryWrapper, CommonConstants.TableName.MED_MEDICATION_DEFINITION,
            CommonConstants.TableName.ADM_DEVICE_DEFINITION, SupplyType.PRODUCT_RETURN.getValue(),
            SupplyStatus.AGREE.getValue(), EventStatus.COMPLETED.getValue());

        return R.ok(purchaseReturnReportPageDto);
    }
}
