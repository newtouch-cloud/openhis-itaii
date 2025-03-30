/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.chargemanage.appservice.IOutpatientRefundAppService;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
     * 查询结算过的就诊患者分页列表
     *
     * @param encounterPatientPageParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param request 请求
     * @return 就诊患者分页列表
     */
    @GetMapping(value = "/encounter-patient-page")
    public R<?> getBilledEncounterPatientPage(EncounterPatientPageParam encounterPatientPageParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return R.ok(outpatientRefundAppService.getBilledEncounterPatientPage(encounterPatientPageParam, searchKey,
            pageNo, pageSize, request));
    }

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
