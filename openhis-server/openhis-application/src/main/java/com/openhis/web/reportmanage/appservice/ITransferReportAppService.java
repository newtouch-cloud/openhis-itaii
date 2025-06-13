/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.dto.TransferReportSearchParam;

/**
 * 商品调拨明细报表 appService
 *
 * @author GYY
 * @date 2025-04-15
 */
public interface ITransferReportAppService {

    /**
     * 商品调拨明细列表
     *
     * @param transferReportSearchParam 商品调拨明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨明细
     */
    R<?> getPage(TransferReportSearchParam transferReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);
}
