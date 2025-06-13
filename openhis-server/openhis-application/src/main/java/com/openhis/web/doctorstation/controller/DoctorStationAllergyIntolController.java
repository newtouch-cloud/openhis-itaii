package com.openhis.web.doctorstation.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.appservice.IDoctorStationAllergyIntolAppService;
import com.openhis.web.doctorstation.dto.AllergyIntoInfoDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 医生站-患者过敏与不耐受管理
 *
 * @author liuhr
 * @date 2025/4/10
 */
@RestController
@RequestMapping("/doctor-station/allergy-intolerance")
@Slf4j
@AllArgsConstructor
public class DoctorStationAllergyIntolController {
    @Autowired
    private IDoctorStationAllergyIntolAppService doctorStationAllergyIntolAppService;

    /**
     * 患者过敏与不耐受数据初始化
     *
     * @return 基础数据
     */
    @GetMapping(value = "/init")
    public R<?> init() {

        return doctorStationAllergyIntolAppService.init();
    }

    /**
     * 查询患者过敏与不耐受信息
     *
     * @param patientId 患者Id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 患者过敏与不耐受信息
     */
    @GetMapping(value = "/allergy-intolerance-info")
    public R<?> getAllergyIntolerance(@RequestParam Long patientId,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        return doctorStationAllergyIntolAppService.getAllergyIntoleranceInfo(patientId, pageNo, pageSize, request);
    }

    /**
     * 作废当条患者过敏与不耐受信息
     *
     * @param allergyIntoInfoDto 患者过敏与不耐受信息
     * @return
     */
    @PutMapping(value = "/allergy-intolerance-info")
    public R<?> invalidateAllergyIntolerance(@RequestBody AllergyIntoInfoDto allergyIntoInfoDto) {

        return doctorStationAllergyIntolAppService.invalidateAllergyIntolerance(allergyIntoInfoDto);
    }

    /**
     * 新增患者过敏与不耐受信息
     *
     * @param allergyIntoInfoDto 患者过敏与不耐受信息
     * @return
     */
    @PostMapping(value = "/allergy-intolerance-info")
    public R<?> addAllergyIntoleranceInfo(@Validated @RequestBody AllergyIntoInfoDto allergyIntoInfoDto) {

        return doctorStationAllergyIntolAppService.addAllergyIntoleranceInfo(allergyIntoInfoDto);
    }

}
