package com.openhis.web.inpatientmanage.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openhis.administration.service.IEncounterService;
import com.openhis.administration.service.IPatientService;
import com.openhis.web.inpatientmanage.dto.AdmissionDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/inpatient-manage")
@Slf4j
@AllArgsConstructor
public class AdmissionController {
    private final IEncounterService encounterService;
    private final IPatientService patientService;

    // 办理入院
    @PostMapping("admission")
    public void addSupplyRequest(@Validated @RequestBody AdmissionDto admission) {

    }
}
