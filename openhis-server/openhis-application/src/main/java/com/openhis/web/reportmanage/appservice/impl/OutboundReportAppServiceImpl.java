package com.openhis.web.reportmanage.appservice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.enums.*;
import com.openhis.web.inventorymanage.dto.PurchaseInventoryInitDto;
import com.openhis.web.reportmanage.dto.OutboundReportInitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.reportmanage.appservice.IOutboundReportAppService;
import com.openhis.web.reportmanage.dto.OutboundReportPageDto;
import com.openhis.web.reportmanage.dto.OutboundReportSearchParam;
import com.openhis.web.reportmanage.mapper.OutboundReportMapper;

/**
 * 入库明细 应用实现类
 *
 * @author yuanzs
 * @date 2025/4/21
 */
@Service
public class OutboundReportAppServiceImpl implements IOutboundReportAppService {

    @Autowired
    OutboundReportMapper outboundReportMapper;

    @Resource
    private ISupplierService supplierService;

    /**
     * 出库明细页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {

        OutboundReportInitDto initDto = new OutboundReportInitDto();
        // 查询供应商列表
        List<Supplier> supplierList = supplierService.getList();

        // 供应商信息
        List<OutboundReportInitDto.supplierListOption> supplierListOptions = supplierList.stream()
                .map(supplier -> new OutboundReportInitDto.supplierListOption(supplier.getId(), supplier.getName()))
                .collect(Collectors.toList());

        initDto.setSupplierListOptions(supplierListOptions);

        return R.ok(initDto);
    }

    /**
     * 出库明细
     *
     * @param outboundReportSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 出库明细分页列表
     */
    @Override
    public R<?> getPage(OutboundReportSearchParam outboundReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<OutboundReportSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(outboundReportSearchParam, searchKey,
                new HashSet<>(Arrays.asList("supply_bus_no", "name", "bus_no")), request);

        // 查询出库明细分页列表
        Page<OutboundReportPageDto> medDetailsPage =
            outboundReportMapper.selectOutboundReportPage(new Page<>(pageNo, pageSize), queryWrapper,
                CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
                SupplyType.ISSUE_INVENTORY.getValue(), SupplyStatus.AGREE.getValue(), EventStatus.COMPLETED.getValue());

        return R.ok(medDetailsPage);
    }

}
