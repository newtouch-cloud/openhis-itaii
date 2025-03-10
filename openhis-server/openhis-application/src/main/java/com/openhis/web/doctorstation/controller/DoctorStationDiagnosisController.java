/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.doctorstation.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.common.enums.BindingType;
import com.openhis.web.doctorstation.appservice.IDoctorStationDiagnosisAppService;
import com.openhis.web.doctorstation.dto.DiagnosisBelongBindingDto;
import com.openhis.web.doctorstation.dto.DiagnosisBelongBindingInitDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 医生站-诊断 controller
 */
@RestController
@RequestMapping("/doctor-station/diagnosis")
@Slf4j
@AllArgsConstructor
public class DoctorStationDiagnosisController {

    private final IDoctorStationDiagnosisAppService iDoctorStationDiagnosisAppService;

    /**
     * 诊断归属绑定基础数据
     * 
     * @return
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        DiagnosisBelongBindingInitDto diagnosisBelongBindingInitDto = new DiagnosisBelongBindingInitDto();
        // 诊断绑定类型 - 用于维护诊断归属绑定关系
        List<DiagnosisBelongBindingInitDto.DiagnosisBelongBindingOption> diagnosisBelongBindingOption =
            Stream.of(BindingType.values())
                .map(status -> new DiagnosisBelongBindingInitDto.DiagnosisBelongBindingOption(status.getValue(),
                    status.getInfo()))
                .collect(Collectors.toList());
        diagnosisBelongBindingInitDto.setDiagnosisBelongBindingOptions(diagnosisBelongBindingOption);
        return R.ok(diagnosisBelongBindingInitDto);
    }

    /**
     * 新增诊断归属绑定
     * 
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    @PostMapping("/diagnosis-belong-binding")
    public R<?> addDiagnosisBelongBinding(@Validated @RequestBody DiagnosisBelongBindingDto diagnosisBelongBindingDto) {
        return iDoctorStationDiagnosisAppService.addDiagnosisBelongBinding(diagnosisBelongBindingDto);
    }

    /**
     * 编辑诊断归属绑定
     *
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    @PutMapping("/diagnosis-belong-binding")
    public R<?>
        updateDiagnosisBelongBinding(@Validated @RequestBody DiagnosisBelongBindingDto diagnosisBelongBindingDto) {
        return iDoctorStationDiagnosisAppService.updateDiagnosisBelongBinding(diagnosisBelongBindingDto);
    }

}
