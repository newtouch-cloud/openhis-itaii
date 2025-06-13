package com.openhis.web.doctorstation.controller;

import com.openhis.web.doctorstation.appservice.IDoctorStationPtDetailsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 医生站-患者详情
 *
 * @author liuhr
 * @date 2025/4/10
 */
@RestController
@RequestMapping("/doctor-station/patient-details")
@Slf4j
@AllArgsConstructor
public class DoctorStationPtDetailsController {

    @Autowired
    private IDoctorStationPtDetailsAppService doctorStationPtDetailsAppService;

    /**
     * 查询患者详情
     *
     * @param encounterId 就诊Id
     * @return 患者详情
     */
    @GetMapping(value = "/patient-details")
    public R<?> getPtDetails(@RequestParam Long encounterId) {

        return doctorStationPtDetailsAppService.getPtDetails(encounterId);
    }




}
