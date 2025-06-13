/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.inventorymanage.dto.PurchaseReturnDetailDto;
import com.openhis.web.inventorymanage.dto.PurchaseReturnSearchParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 采购退货 appService
 *
 * @author yuanzs
 * @date 2025-04-02
 */
public interface IPurchaseReturnAppService {

    /**
     * 采购退货页面初始化
     *
     * @return 初始化信息
     */
    R<?> purchaseReturnInit();

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    R<?> purchaseNoInit();

    /**
     * 根据单据号查询退货单据详情
     *
     * @param busNo 单据号
     * @return 退货单
     */
    R<?> getDetail(String busNo);

    /**
     * 根据单据号查询已生成的退货单
     *
     * @param busNo 单据号
     * @return 退货单
     */
    R<?> getGeneratedPage(String busNo);

    /**
     * 生成退货单(批量)
     *
     * @param purchaseReturnDetailDtoList 退货单据号列表
     * @return 操作结果
     */
    R<?> returnGenerateBusNo(List<PurchaseReturnDetailDto> purchaseReturnDetailDtoList);

    /**
     * 删除退回退货单
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    R<?> returnDeleteBusNo(List<Long> supplyRequestIds);

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    R<?> submitApproval(String busNo);

    /**
     * 驳回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    R<?> withdrawApproval(String busNo);
}
