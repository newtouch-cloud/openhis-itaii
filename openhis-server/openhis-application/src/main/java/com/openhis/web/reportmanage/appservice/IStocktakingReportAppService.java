/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.dto.StocktakingReportSearchParam;

/**
 * 库存盘点明细报表 appService
 *
 * @author GYY
 * @date 2025-04-16
 */
public interface IStocktakingReportAppService {

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
    R<?> getPage(StocktakingReportSearchParam stocktakingReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);
}
