package com.openhis.web.patientmanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.patientmanage.appservice.IPatientInformationService;
import com.openhis.web.patientmanage.dto.PatientInfoSearchParam;
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
@RequestMapping("/patient-manage/information")
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
     * @param patientInfoSearchParam 患者查询参数
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    @GetMapping("/patient-information-page")
    public R<?> getPatient(PatientInfoSearchParam patientInfoSearchParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        return R.ok(patientInformationService.getPatientInfo(patientInfoSearchParam, searchKey, pageNo, pageSize,request));
    }

}
