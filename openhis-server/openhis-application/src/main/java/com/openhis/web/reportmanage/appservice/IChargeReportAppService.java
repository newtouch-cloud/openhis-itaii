/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.dto.ChargeReportSearchParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 费用明细报表 appService
 *
 * @author yuxj
 * @date 2025-05-20
 */
public interface IChargeReportAppService {
    /**
     * 下拉框
     *
     * @return 下拉框信息
     */
    R<?> chargeReportInit();

    /**
     * 费用明细列表
     *
     * @param chargeReportSearchParam 费用明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 费用明细
     */
    R<?> getPage(ChargeReportSearchParam chargeReportSearchParam, Integer pageNo, Integer pageSize,
                 String searchKey, HttpServletRequest request);
}
