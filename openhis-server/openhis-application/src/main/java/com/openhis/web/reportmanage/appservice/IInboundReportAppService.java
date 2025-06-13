/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.dto.InboundReportSearchParam;

/**
 * 入库明细报表 appService
 *
 * @author GYY
 * @date 2025-04-22
 */
public interface IInboundReportAppService {

    /**
     * 入库明细页面初始化
     *
     * @return 初始化信息
     */
    R<?> init();

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
    R<?> getPage(InboundReportSearchParam inboundReportSearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request);
}
