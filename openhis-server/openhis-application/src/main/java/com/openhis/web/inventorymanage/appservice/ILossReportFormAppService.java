/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.LossReportFormDto;
import com.openhis.web.inventorymanage.dto.LossReportSearchParam;

import java.util.List;

/**
 * 报损单 appService
 *
 * @author gyy
 * @date 2025-04-07
 */
public interface ILossReportFormAppService {

    /**
     * 报损单单据列表
     *
     * @param lossReportSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨单据分页列表
     */
    R<?> getPage(LossReportSearchParam lossReportSearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request);

    /**
     * 报损单页面初始化
     *
     * @return 初始化信息
     */
    R<?> lossReportFormInit();

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    R<?> lossReportNoInit();

    /**
     * 报损单据详情
     *
     * @param busNo 单据号
     * @return 报损详情
     */
    R<?> getDetail(String busNo);

    /**
     * 添加/编辑报损单据(批量)
     *
     * @param lossReportFormDtoList 报损单据
     * @return 操作结果
     */
    R<?> addOrEditLossReceipt(List<LossReportFormDto> lossReportFormDtoList);

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
