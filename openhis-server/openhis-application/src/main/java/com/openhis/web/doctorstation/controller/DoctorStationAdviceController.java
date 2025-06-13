/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.doctorstation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.common.enums.AdviceOpType;
import com.openhis.common.enums.Whether;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.AdviceSaveParam;
import com.openhis.web.doctorstation.dto.UpdateGroupIdParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 医生站-医嘱/处方 controller
 */
@RestController
@RequestMapping("/doctor-station/advice")
@Slf4j
@AllArgsConstructor
public class DoctorStationAdviceController {

    private final IDoctorStationAdviceAppService iDoctorStationAdviceAppService;

    /**
     * 查询医嘱信息
     *
     * @param adviceBaseDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param locationId 药房id
     * @param adviceDefinitionIdParamList 医嘱定义id参数集合
     * @param organizationId 患者挂号对应的科室id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 医嘱信息
     */
    @GetMapping(value = "/advice-base-info")
    public R<?> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "locationId", required = false) Long locationId,
        @RequestParam(value = "adviceDefinitionIdParamList", required = false) List<Long> adviceDefinitionIdParamList,
        @RequestParam(value = "organizationId") Long organizationId,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iDoctorStationAdviceAppService.getAdviceBaseInfo(adviceBaseDto, searchKey, locationId,
            adviceDefinitionIdParamList, organizationId, pageNo, pageSize, Whether.NO.getValue()));
    }

    /**
     * 门诊保存医嘱
     * 
     * @param adviceSaveParam 医嘱表单信息
     * @return 结果
     */
    @PostMapping(value = "/save-advice")
    public R<?> saveAdvice(@RequestBody AdviceSaveParam adviceSaveParam) {
        return iDoctorStationAdviceAppService.saveAdvice(adviceSaveParam, AdviceOpType.SAVE_ADVICE.getCode());
    }

    /**
     * 门诊签发医嘱
     *
     * @param adviceSaveParam 医嘱表单信息
     * @return 结果
     */
    @PostMapping(value = "/sign-advice")
    public R<?> signAdvice(@RequestBody AdviceSaveParam adviceSaveParam) {
        return iDoctorStationAdviceAppService.saveAdvice(adviceSaveParam, AdviceOpType.SIGN_ADVICE.getCode());
    }

    /**
     * 门诊签退医嘱
     *
     * @param requestIdList 请求id列表
     * @return 结果
     */
    @PostMapping(value = "/sign-off")
    public R<?> signOffAdvice(@RequestBody List<Long> requestIdList) {
        return iDoctorStationAdviceAppService.signOffAdvice(requestIdList);
    }

    /**
     * 查询医嘱请求数据
     * 
     * @param encounterId 就诊id
     * @return 医嘱请求数据
     */
    @GetMapping(value = "/request-base-info")
    public R<?> getRequestBaseInfo(@RequestParam Long encounterId) {
        return iDoctorStationAdviceAppService.getRequestBaseInfo(encounterId);
    }

    /**
     * 查询历史医嘱请求数据
     *
     * @param patientId 病人id
     * @return 历史医嘱请求数据
     */
    @GetMapping(value = "/request-history-info")
    public R<?> getRequestHistoryInfo(@RequestParam Long patientId, Long encounterId) {
        return iDoctorStationAdviceAppService.getRequestHistoryInfo(patientId, encounterId);
    }

    /**
     * 更新组号
     * 
     * @param updateGroupIdParam 更新组号参数
     */
    @PutMapping(value = "/update-groupid")
    public void updateGroupId(@RequestBody UpdateGroupIdParam updateGroupIdParam) {
        iDoctorStationAdviceAppService.updateGroupId(updateGroupIdParam);
    }

    /**
     * 查询就诊费用性质
     *
     * @param encounterId 就诊id
     * @return 就诊费用性质
     */
    @GetMapping(value = "/get-encounter-contract")
    public R<?> getEncounterContract(@RequestParam Long encounterId) {
        return iDoctorStationAdviceAppService.getEncounterContract(encounterId);
    }

}
