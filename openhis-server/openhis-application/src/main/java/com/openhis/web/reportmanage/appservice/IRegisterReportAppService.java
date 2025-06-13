/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.dto.RegisterReportSearchParam;
import com.openhis.web.reportmanage.dto.TransferReportSearchParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 挂号明细报表 appService
 *
 * @author yuxj
 * @date 2025-05-20
 */
public interface IRegisterReportAppService {
    /**
     * 下拉框
     *
     * @return 下拉框信息
     */
    R<?> registerReportInit();

    /**
     * 挂号明细列表
     *
     * @param registerReportSearchParam 挂号明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 挂号明细
     */
    R<?> getPage(RegisterReportSearchParam registerReportSearchParam, Integer pageNo, Integer pageSize,
                 String searchKey, HttpServletRequest request);
}
