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

import com.openhis.administration.domain.Supplier;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.enums.*;
import com.openhis.web.reportmanage.dto.InboundReportInitDto;
import com.openhis.web.reportmanage.dto.OutboundReportInitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.reportmanage.appservice.IInboundReportAppService;
import com.openhis.web.reportmanage.dto.InboundReportPageDto;
import com.openhis.web.reportmanage.dto.InboundReportSearchParam;
import com.openhis.web.reportmanage.mapper.InboundReportMapper;

/**
 * 入库明细报表 impl
 *
 * @author GYY
 * @date 2025-04-22
 */
@Service
public class InboundReportAppServiceImpl implements IInboundReportAppService {

    @Autowired
    private InboundReportMapper inboundReportMapperMapper;

    @Resource
    private ISupplierService supplierService;

    /**
     * 入库明细页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {

        InboundReportInitDto initDto = new InboundReportInitDto();
        // 查询供应商列表
        List<Supplier> supplierList = supplierService.getList();

        // 供应商信息
        List<InboundReportInitDto.supplierListOption> supplierListOptions = supplierList.stream()
                .map(supplier -> new InboundReportInitDto.supplierListOption(supplier.getId(), supplier.getName()))
                .collect(Collectors.toList());

        initDto.setSupplierListOptions(supplierListOptions);

        return R.ok(initDto);
    }

    /**
     * 入库明细列表
     *
     * @param inboundReportSearchParam 入库明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 入库明细
     */
    @Override
    public R<?> getPage(InboundReportSearchParam inboundReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<InboundReportSearchParam> queryWrapper = HisQueryUtils.buildQueryWrapper(inboundReportSearchParam,
            searchKey, new HashSet<>(Arrays.asList("supply_bus_no", "name", "bus_no")), request);

        // 查询入库明细分页列表
        Page<InboundReportPageDto> inboundReportPage = inboundReportMapperMapper.selectInboundReportPage(
            new Page<>(pageNo, pageSize), queryWrapper, CommonConstants.TableName.MED_MEDICATION_DEFINITION,
            CommonConstants.TableName.ADM_DEVICE_DEFINITION, SupplyType.PURCHASE_INVENTORY.getValue(),
            SupplyStatus.AGREE.getValue(), EventStatus.COMPLETED.getValue());

        return R.ok(inboundReportPage);
    }
}
