/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.chargemanage.appservice.IOutpatientChargeAppService;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 门诊收费 controller
 *
 * @author zwh
 * @date 2025-03-12
 */
@RestController
@RequestMapping("/charge-manage/charge")
@Slf4j
@AllArgsConstructor
public class OutpatientChargeController {

    @Autowired
    private IOutpatientChargeAppService outpatientChargeAppService;

    /**
     * 查询就诊患者分页列表
     *
     * @param encounterPatientPageParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param request 请求
     * @return 就诊患者分页列表
     */
    @GetMapping(value = "/encounter-patient-page")
    public R<?> getEncounterPatientPage(EncounterPatientPageParam encounterPatientPageParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return R.ok(outpatientChargeAppService.getEncounterPatientPage(encounterPatientPageParam, searchKey, pageNo,
            pageSize, request));
    }

    /**
     * 根据就诊id查询患者处方列表
     *
     * @param encounterId 就诊id
     * @return 患者处方列表
     */
    @GetMapping(value = "/patient-prescription")
    public R<?> getEncounterPatientPrescription(@RequestParam Long encounterId) {
        return R.ok(outpatientChargeAppService.getEncounterPatientPrescription(encounterId));
    }

    /**
     * 医保转自费
     *
     * @param encounterId 就诊id
     * @return 操作结果
     */
    @PutMapping("/self-pay")
    public R<?> changeToSelfPay(@RequestParam Long encounterId) {
        return outpatientChargeAppService.changeToSelfPay(encounterId);
    }

    /**
     * 自费转医保
     *
     * @param encounterId 就诊id
     * @return 操作结果
     */
    @PutMapping("/medical-insurance")
    public R<?> changeToMedicalInsurance(@RequestParam Long encounterId) {
        return outpatientChargeAppService.changeToMedicalInsurance(encounterId);
    }
}
