/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.doctorstation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;

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

    @GetMapping(value = "/advice-base-info")
    public R<?> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "locationId") Long locationId,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return null;
    }

}
