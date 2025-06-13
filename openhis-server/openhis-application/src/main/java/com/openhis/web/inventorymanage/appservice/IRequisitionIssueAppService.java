/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.IssueDto;
import com.openhis.web.inventorymanage.dto.IssueSearchParam;
import com.openhis.web.inventorymanage.dto.SupplySearchParam;

import java.util.List;

/**
 * 领用出库 appService
 *
 * @author CY
 * @date 2025-04-03
 */
public interface IRequisitionIssueAppService {

    /**
     * 领用出库单据页面初始化
     *
     * @return 初始化信息
     */
    R<?> issueInventoryInit();

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    R<?> requisitionIssueNoInit();

    /**
     * 领用出库单据列表
     *
     * @param issueSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 入库单据分页列表
     */
    R<?> getPage(IssueSearchParam issueSearchParam, Integer pageNo, Integer pageSize, String searchKey,
                 HttpServletRequest request);

    /**
     * 领用出库单据详情
     *
     * @param busNo 单据号
     * @return 领用出库据详情
     */
    R<?> getDetail(String busNo);

    /**
     * 添加/编辑入库单据(批量)
     *
     * @param requisitionIssueDtoList 入库单据
     * @return 操作结果
     */
    R<?> addOrEditIssueReceipt(List<IssueDto> requisitionIssueDtoList);

    /**
     * 删除单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    R<?> deleteReceipt(List<Long> supplyRequestIds);

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    R<?> submitApproval(String busNo);

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    R<?> withdrawApproval(String busNo);

}
