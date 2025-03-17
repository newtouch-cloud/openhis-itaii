package com.openhis.web.patientmanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.patientmanage.appservice.IPatientInformationService;
import com.openhis.web.patientmanage.dto.PatientInformationDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 病人管理——病人信息
 *
 * @author liuhr
 * @date 2025/2/22
 */
@RestController
@RequestMapping("/patientmanage/information")
@Slf4j
@AllArgsConstructor
public class PatientInformationController {

    @Autowired
    IPatientInformationService patientInformationService;

    /**
     * 病人信息记录初期数据列表
     *
     * @return
     */
    @GetMapping("/init")
    public R<?> getPatientInfoInit() {

        return R.ok(patientInformationService.getPatientInfoInit());
    }

    /**
     * 添加病人信息
     *
     * @param patientInformationDto 病人信息
     */
    @PostMapping("/patient-information")
    public R<?> addPatient(@Validated @RequestBody PatientInformationDto patientInformationDto) {
        return patientInformationService.addPatient(patientInformationDto);
    }

    /**
     * 修改病人信息
     *
     * @param patientInformationDto 病人信息
     */
    @PutMapping("/patient-information")
    public R<?> editPatient(@Validated @RequestBody PatientInformationDto patientInformationDto) {
        // 调用服务层更新病人信息
        return patientInformationService.editPatient(patientInformationDto);

    }

    /**
     * 分页查询病人信息,可选条件
     *
     * @param busNo 病人ID（可选）
     * @param name 病人姓名（可选）
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    @GetMapping("/patient-information-page")
    public R<?> getPatient(@RequestParam(required = false) String busNo, @RequestParam(required = false) String name,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return R.ok(patientInformationService.getPatient(busNo, name, pageNo, pageSize));
    }

}
