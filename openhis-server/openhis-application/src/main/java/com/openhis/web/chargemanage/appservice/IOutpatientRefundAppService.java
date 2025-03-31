/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;

/**
 * 门诊退费 service
 *
 * @author zwh
 * @date 2025-03-15
 */
public interface IOutpatientRefundAppService {

    /**
     * 根据就诊id查询患者的账单
     *
     * @param encounterId 就诊id
     * @return 患者账单列表
     */
    R<?> getEncounterPatientPayment(Long encounterId);

    /**
     * 根据账单退费
     *
     * @param paymentIdList 付费id列表
     * @return 操作结果
     */
    R<?> refundPayment(List<Long> paymentIdList);

    /**
     * 查询结算过的就诊患者分页列表
     *
     * @param encounterPatientPageParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param request 请求
     * @return 就诊患者分页列表
     */
    R<?> getBilledEncounterPatientPage(EncounterPatientPageParam encounterPatientPageParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 门诊退费页面初始化
     *
     * @return 初始化信息
     */
    R<?> outpatientRefundInit();
}
