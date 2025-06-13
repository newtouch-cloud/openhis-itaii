/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import com.openhis.common.enums.EventStatus;
import com.openhis.web.inventorymanage.dto.ProductDetailsSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.CategoryType;
import com.openhis.common.enums.SupplyStatus;
import com.openhis.common.enums.SupplyType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.reportmanage.appservice.IStocktakingReportAppService;
import com.openhis.web.reportmanage.dto.StocktakingReportPageDto;
import com.openhis.web.reportmanage.dto.StocktakingReportSearchParam;
import com.openhis.web.reportmanage.mapper.StocktakingReportMapper;

/**
 * 库存盘点明细报表 impl
 *
 * @author GYY
 * @date 2025-04-16
 */
@Service
public class StocktakingReportAppServiceImpl implements IStocktakingReportAppService {

    @Autowired
    private StocktakingReportMapper stocktakingReportMapper;

    /**
     * 库存盘点明细列表
     *
     * @param stocktakingReportSearchParam 库存盘点明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 库存盘点明细
     */
    @Override
    public R<?> getPage(StocktakingReportSearchParam stocktakingReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<StocktakingReportSearchParam> queryWrapper = HisQueryUtils.buildQueryWrapper(
            stocktakingReportSearchParam, searchKey, new HashSet<>(Arrays.asList("name", "bus_no")), request);

        // 查询库存盘点明细分页列表
        Page<StocktakingReportPageDto> stocktakingReportPage =
            stocktakingReportMapper.selectStocktakingReportPage(new Page<>(pageNo, pageSize), queryWrapper,
                CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
                SupplyType.PRODUCT_STOCKTAKING.getValue(), SupplyType.PRODUCT_BATCH_STOCKTAKING.getValue(),
                SupplyStatus.AGREE.getValue(), EventStatus.COMPLETED.getValue());

        // stocktakingReportPage.getRecords().forEach(e -> {
        // // 药品类型
        // e.setItemTableText(EnumUtils.getInfoByValue(CategoryType.class, e.getItemTable()));
        // });
        return R.ok(stocktakingReportPage);
    }
}
