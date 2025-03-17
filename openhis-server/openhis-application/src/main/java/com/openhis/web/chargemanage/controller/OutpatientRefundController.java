/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.chargemanage.appservice.IOutpatientRefundAppService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 门诊退费 controller
 *
 * @author zwh
 * @date 2025-03-15
 */
@RestController
@RequestMapping("/charge-manage/refund")
@Slf4j
@AllArgsConstructor
public class OutpatientRefundController {

    @Autowired
    private IOutpatientRefundAppService outpatientRefundAppService;

    /**
     * 根据就诊id查询患者的账单
     *
     * @param encounterId 就诊id
     * @return 患者账单列表
     */
    @GetMapping(value = "/patient-payment")
    public R<?> getEncounterPatientPayment(@RequestParam Long encounterId) {
        return R.ok(outpatientRefundAppService.getEncounterPatientPayment(encounterId));
    }

    /**
     * 根据账单退费
     *
     * @param paymentIdList 付费id列表
     * @return 操作结果
     */
    @PostMapping(value = "/refund-payment")
    public R<?> refundPayment(@RequestParam List<Long> paymentIdList) {
        return R.ok(outpatientRefundAppService.refundPayment(paymentIdList));
    }

}
