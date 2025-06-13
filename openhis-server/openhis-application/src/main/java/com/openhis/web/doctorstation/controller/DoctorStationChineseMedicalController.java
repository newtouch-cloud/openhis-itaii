/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.doctorstation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.common.enums.AdviceOpType;
import com.openhis.common.enums.Whether;
import com.openhis.web.doctorstation.appservice.IDoctorStationChineseMedicalAppService;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.AdviceSaveParam;
import com.openhis.web.doctorstation.dto.SaveDiagnosisParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 医生站-中医 controller
 */
@RestController
@RequestMapping("/doctor-station/chinese-medical")
@Slf4j
@AllArgsConstructor
public class DoctorStationChineseMedicalController {

    private final IDoctorStationChineseMedicalAppService iDoctorStationChineseMedicalAppService;

    /**
     * 查询中医诊断数据
     * 
     * @param searchKey 关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 中医诊断数据
     */
    @GetMapping(value = "/condition-info")
    public R<?> getConditionInfo(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iDoctorStationChineseMedicalAppService.getConditionInfo(searchKey, pageNo, pageSize));
    }

    /**
     * 查询中医证候数据
     *
     * @param searchKey 关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 中医证候数据
     */
    @GetMapping(value = "/syndrome-info")
    public R<?> getSyndromeInfo(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iDoctorStationChineseMedicalAppService.getSyndromeInfo(searchKey, pageNo, pageSize));
    }

    /**
     * 保存中医诊断
     *
     * @param saveDiagnosisParam 诊断信息
     * @return 结果
     */
    @PostMapping(value = "/save-tcm-diagnosis")
    public R<?> saveTcmDiagnosis(@RequestBody SaveDiagnosisParam saveDiagnosisParam) {
        return iDoctorStationChineseMedicalAppService.saveTcmDiagnosis(saveDiagnosisParam);
    }

    /**
     * 查询中医就诊诊断信息
     *
     * @param encounterId 就诊id
     * @return 中医就诊诊断信息
     */
    @GetMapping(value = "/get-tcm-encounter-diagnosis")
    public R<?> getTcmEncounterDiagnosis(@RequestParam Long encounterId) {
        return iDoctorStationChineseMedicalAppService.getTcmEncounterDiagnosis(encounterId);
    }

    /**
     * 查询中医医嘱信息
     *
     * @param adviceBaseDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param locationId 药房id
     * @param adviceDefinitionIdParamList 医嘱定义id参数集合
     * @param organizationId 患者挂号对应的科室id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 中医医嘱信息
     */
    @GetMapping(value = "/tcm-advice-base-info")
    public R<?> getTcmAdviceBaseInfo(AdviceBaseDto adviceBaseDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "locationId", required = false) Long locationId,
        @RequestParam(value = "adviceDefinitionIdParamList", required = false) List<Long> adviceDefinitionIdParamList,
        @RequestParam(value = "organizationId") Long organizationId,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iDoctorStationChineseMedicalAppService.getTcmAdviceBaseInfo(adviceBaseDto, searchKey, locationId,
            adviceDefinitionIdParamList, organizationId, pageNo, pageSize, Whether.NO.getValue()));
    }

    /**
     * 门诊保存中医医嘱
     *
     * @param adviceSaveParam 医嘱表单信息
     * @return 结果
     */
    @PostMapping(value = "/save-tcm-advice")
    public R<?> saveTcmAdvice(@RequestBody AdviceSaveParam adviceSaveParam) {
        return iDoctorStationChineseMedicalAppService.saveOrSignTcmAdvice(adviceSaveParam,
            AdviceOpType.SAVE_ADVICE.getCode());
    }

}
