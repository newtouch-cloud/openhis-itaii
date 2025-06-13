/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice.impl;

import javax.servlet.http.HttpServletRequest;

import com.openhis.common.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.enums.CategoryType;
import com.openhis.common.enums.ConditionCode;
import com.openhis.common.enums.LocationForm;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.reportmanage.appservice.IInventoryProductReportAppService;
import com.openhis.web.reportmanage.dto.InventoryProductReportPageDto;
import com.openhis.web.reportmanage.dto.InventoryProductReportSearchParam;
import com.openhis.web.reportmanage.mapper.InventoryProductReportMapper;

import java.util.HashSet;

/**
 * 库存商品明细报表 impl
 *
 * @author GYY
 * @date 2025-04-21
 */
@Service
public class InventoryProductReportAppServiceImpl implements IInventoryProductReportAppService {

    @Autowired
    private InventoryProductReportMapper inventoryProductReportMapper;

    /**
     * 库存商品明细列表
     *
     * @param inventoryProductReportSearchParam 库存商品明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 库存商品明细
     */
    @Override
    public R<?> getPage(InventoryProductReportSearchParam inventoryProductReportSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.BusNo);

        // 构建查询条件
        QueryWrapper<InventoryProductReportSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(inventoryProductReportSearchParam, searchKey, searchFields, request);

        // 查询库存商品明细分页列表
        Page<InventoryProductReportPageDto> productReportPage = inventoryProductReportMapper.selectProductReportPage(
            new Page<>(pageNo, pageSize), queryWrapper, ConditionCode.LOT_NUMBER.getValue().toString());

        productReportPage.getRecords().forEach(e -> {
            // 药品类型
            e.setItemTableText(EnumUtils.getInfoByValue(CategoryType.class, e.getItemTable()));
            // 仓库类型
            e.setPurposeTypeEnum_enumText(EnumUtils.getInfoByValue(LocationForm.class, e.getPurposeTypeEnum()));
        });
        return R.ok(productReportPage);
    }
}