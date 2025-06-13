/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import com.openhis.web.reportmanage.appservice.ILossReportAppService;
import com.openhis.web.reportmanage.dto.LossReportPageDto;
import com.openhis.web.reportmanage.dto.LossReportSearchParam;
import com.openhis.web.reportmanage.mapper.LossReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.EventStatus;
import com.openhis.common.enums.SupplyStatus;
import com.openhis.common.enums.SupplyType;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.reportmanage.appservice.IStocktakingReportAppService;
import com.openhis.web.reportmanage.dto.StocktakingReportPageDto;
import com.openhis.web.reportmanage.dto.StocktakingReportSearchParam;
import com.openhis.web.reportmanage.mapper.StocktakingReportMapper;

/**
 * 报损明细报表 impl
 *
 * @author ym
 * @date 2025-05-21
 */
@Service
public class LossReportAppServiceImpl implements ILossReportAppService {

    @Autowired
    private LossReportMapper lossReportMapper;

    /**
     * 报损明细列表
     *
     * @param lossReportSearchParam 报损明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 报损明细
     */
    @Override
    public R<?> getPage(LossReportSearchParam lossReportSearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<LossReportSearchParam> queryWrapper = HisQueryUtils.buildQueryWrapper(lossReportSearchParam,
            searchKey, new HashSet<>(Arrays.asList("name", "bus_no")), request);

        // 查询报损明细分页列表
        Page<LossReportPageDto> lossReportPage = lossReportMapper.selectLossReportPage(new Page<>(pageNo, pageSize),
            queryWrapper, CommonConstants.TableName.MED_MEDICATION_DEFINITION,
            CommonConstants.TableName.ADM_DEVICE_DEFINITION, SupplyType.LOSS_REPORT_FORM.getValue(),
            SupplyStatus.AGREE.getValue(), EventStatus.COMPLETED.getValue());

        return R.ok(lossReportPage);
    }
}
