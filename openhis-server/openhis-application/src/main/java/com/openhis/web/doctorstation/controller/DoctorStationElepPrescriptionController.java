/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.doctorstation.controller;

import com.openhis.web.doctorstation.appservice.IDoctorStationElepPrescriptionService;
import com.openhis.web.doctorstation.dto.*;
import com.openhis.web.ybmanage.service.IYbEleHttpService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.common.enums.AdviceOpType;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 医生站-电子处方 controller
 */
@RestController
@RequestMapping("/doctor-station/elep")
@Slf4j
@AllArgsConstructor
public class DoctorStationElepPrescriptionController {

    @Autowired
    IDoctorStationElepPrescriptionService doctorStationElepService;

    /**
     * 电子处方下拉框
     *
     * @return 下拉框信息
     */
    @GetMapping("/init")
    public R<?> elepPrescriptionInit() {
        return doctorStationElepService.elepPrescriptionInit();
    }

    /**
     * 获取全部药品信息
     *
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 药品信息
     */
    @GetMapping("/get-allMedicationInfo")
    public R<?> getAllMedicationInfo(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return doctorStationElepService.getAllMedicationInfo(searchKey, pageNo, pageSize);
    }

    /**
     * 获取处方信息
     *
     * @param patientId 患者id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 药品信息
     */
    @GetMapping("/get-prescriptionInfo")
    public R<?> getPrescriptionInfo(@RequestParam Long patientId,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return doctorStationElepService.getPrescriptionInfo(patientId, pageNo, pageSize);
    }

    /**
     * 获取药品信息
     *
     * @param prescriptionNo 处方号
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 药品详细信息
     */
    @GetMapping("/get-medicationInfo")
    public R<?> getMedicationInfo(@RequestParam String prescriptionNo,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return doctorStationElepService.getMedicationInfo(prescriptionNo, pageNo, pageSize);
    }

    /**
     * 获取单据号
     *
     * @return 初始化信息
     */
    @GetMapping("/prescriptionNoInit")
    public R<?> prescriptionNoInit() {
        return doctorStationElepService.prescriptionNoInit();
    }

    /**
     * 保存处方
     *
     * @param prescriptionInfo 处方信息
     * @return 执行结果
     */
    @PostMapping("/save-prescriptionInfo")
    public R<?> savePrescriptionInfo(@RequestBody ElepPrescriptionInfoParam prescriptionInfo) {
        return doctorStationElepService.savePrescriptionInfo(prescriptionInfo);
    }

    /**
     * 修改处方
     *
     * @param prescriptionInfo 处方信息
     * @return 执行结果
     */
    @PostMapping("/update-prescriptionInfo")
    public R<?> updatePrescriptionInfo(@RequestBody ElepPrescriptionInfoParam prescriptionInfo) {
        return doctorStationElepService.updatePrescriptionInfo(prescriptionInfo);
    }

    /**
     * 删除处方
     *
     * @param deletePrescriptionInfoParam 处方信息
     * @return 执行结果
     */
    @PostMapping("/delete-prescriptionInfo")
    public R<?> deletePrescriptionInfo(@RequestBody DeletePrescriptionInfoParam deletePrescriptionInfoParam) {
        return doctorStationElepService.deletePrescriptionInfo(deletePrescriptionInfoParam);
    }

    /**
     * 签发处方
     *
     * @param prescriptionNoList 处方号
     * @return 执行结果
     */
    @PostMapping("/issuance-prescription")
    public R<?> issuancePrescription(@RequestBody List<String> prescriptionNoList) {
        return doctorStationElepService.issuancePrescription(prescriptionNoList);
    }
}
