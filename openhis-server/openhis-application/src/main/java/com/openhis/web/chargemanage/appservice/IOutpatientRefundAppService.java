/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice;

import com.core.common.core.domain.R;

import java.util.List;

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
}
