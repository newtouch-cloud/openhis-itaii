/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.dto.PurchaseReturnReportSearchParam;

/**
 * 采购退货明细报表 appService
 *
 * @author ym
 * @date 2025-05-23
 */
public interface PurchaseReturnReportAppService {

    /**
     * 采购退货明细页面初始化
     *
     * @return 初始化信息
     */
    R<?> init();

    /**
     * 采购退货明细列表
     *
     * @param purchaseReturnReportSearchParam 采购退货明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 采购退货明细
     */
    R<?> getPage(PurchaseReturnReportSearchParam purchaseReturnReportSearchParam, Integer pageNo, Integer pageSize, String searchKey,
                 HttpServletRequest request);
}
