/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.doctorstation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.appservice.IDoctorStationEmrAppService;
import com.openhis.web.doctorstation.dto.DoctorStationInitDto;
import com.openhis.web.doctorstation.dto.PatientInfoDto;

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
     * 医生站基础数据初始化
     * 
     * @return 基础数据
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        // DoctorStationInitDto doctorStationInitDto = new DoctorStationInitDto();
        return R.ok(new DoctorStationInitDto());
    }

    /**
     * 查询就诊患者信息
     * 
     * @param patientInfoDto 查询条件 (前端传 statusEnum 区分就诊状态tab)
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 就诊患者信息
     */
    @GetMapping(value = "/patient-info")
    public R<?> getPatientInfo(PatientInfoDto patientInfoDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iDoctorStationEmrAppService.getPatientInfo(patientInfoDto, searchKey, pageNo, pageSize));
    }

}
