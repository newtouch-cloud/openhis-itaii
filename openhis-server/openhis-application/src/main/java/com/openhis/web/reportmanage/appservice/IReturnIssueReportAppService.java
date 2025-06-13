package com.openhis.web.reportmanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.dto.ReturnIssueReportSearchParam;

/**
 * 领用退库明细报表 appService
 *
 * @author ym
 * @date 2025-05-23
 */
public interface IReturnIssueReportAppService {

    /**
     * 领用退库明细页面初始化
     *
     * @return 初始化信息
     */
    R<?> init();

    /**
     * 领用退库明细
     *
     * @param returnIssueReportSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 领用退库明细分页列表
     */
    R<?> getPage(ReturnIssueReportSearchParam returnIssueReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);

}
