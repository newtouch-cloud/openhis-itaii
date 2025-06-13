package com.openhis.web.reportmanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.dto.OutboundReportSearchParam;

/**
 * 出库明细报表 appService
 *
 * @author yuanzs
 * @date 2025-04-21
 */
public interface IOutboundReportAppService {

    /**
     * 出库明细页面初始化
     *
     * @return 初始化信息
     */
    R<?> init();

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
    R<?> getPage(OutboundReportSearchParam outboundReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);

}
