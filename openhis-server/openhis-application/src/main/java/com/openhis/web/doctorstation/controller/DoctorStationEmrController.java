/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.doctorstation.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.appservice.IDoctorStationEmrAppService;
import com.openhis.web.doctorstation.dto.EmrTemplateDto;
import com.openhis.web.doctorstation.dto.PatientEmrDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 医生站-电子病历 controller
 */
@RestController
@RequestMapping("/doctor-station/emr")
@Slf4j
@AllArgsConstructor
public class DoctorStationEmrController {

    private final IDoctorStationEmrAppService iDoctorStationEmrAppService;

    /**
     * 添加病人病历信息
     *
     * @param patientEmrDto 电子病历信息dto
     * @return 操作结果
     */
    @PostMapping("/emr")
    public R<?> addPatientEmr(@Validated @RequestBody PatientEmrDto patientEmrDto) {
        return iDoctorStationEmrAppService.addPatientEmr(patientEmrDto);
    }

    /**
     * 获取患者历史病历
     *
     * @param patientEmrDto 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 分页数据列表
     */
    @GetMapping("/emr-page")
    public R<?> getPatientEmrHistory(PatientEmrDto patientEmrDto,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return iDoctorStationEmrAppService.getPatientEmrHistory(patientEmrDto, pageNo, pageSize);
    }

    /**
     * 获取患者病历详情
     *
     * @param encounterId 就诊id
     * @return 病历详情
     */
    @GetMapping("/emr-detail")
    public R<?> getEmrDetail(@RequestParam(value = "encounterId") Long encounterId) {
        return iDoctorStationEmrAppService.getEmrDetail(encounterId);
    }

    /**
     * 保存病历模板
     *
     * @param emrTemplateDto 病历模板信息
     * @return 操作结果
     */
    @PostMapping("emr-template")
    public R<?> addEmrTemplate(@RequestBody @Validated EmrTemplateDto emrTemplateDto) {
        return iDoctorStationEmrAppService.addEmrTemplate(emrTemplateDto);
    }

    /**
     * 获取电子病历模板列表
     *
     * @param emrTemplateDto 查询参数
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("emr-template-page")
    public R<?> getEmrTemplate(EmrTemplateDto emrTemplateDto,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return iDoctorStationEmrAppService.getEmrTemplate(emrTemplateDto, pageNo, pageSize);
    }

    /**
     * 删除病历模板
     *
     * @param id 模板id
     * @return 操作结果
     */
    @DeleteMapping("emr-template")
    public R<?> deleteEmrTemplate(@RequestParam(value = "id") Long id) {
        return iDoctorStationEmrAppService.deleteEmrTemplate(id);
    }

}
