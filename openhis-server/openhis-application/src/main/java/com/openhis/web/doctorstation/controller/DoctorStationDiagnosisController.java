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
import com.openhis.common.enums.ConditionVerificationStatus;
import com.openhis.web.doctorstation.appservice.IDoctorStationDiagnosisAppService;
import com.openhis.web.doctorstation.dto.DiagnosisBelongBindingDto;
import com.openhis.web.doctorstation.dto.DiagnosisBelongBindingInitDto;
import com.openhis.web.doctorstation.dto.SaveDiagnosisParam;

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
     * 诊断相关数据初始化
     * 
     * @return
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        DiagnosisBelongBindingInitDto diagnosisBelongBindingInitDto = new DiagnosisBelongBindingInitDto();
        // 诊断绑定类型 - 用于维护诊断归属绑定关系
        List<DiagnosisBelongBindingInitDto.DiagnosisBelongBindingOption> diagnosisBelongBindingOptions =
            Stream.of(BindingType.values())
                .map(status -> new DiagnosisBelongBindingInitDto.DiagnosisBelongBindingOption(status.getValue(),
                    status.getInfo()))
                .collect(Collectors.toList());
        diagnosisBelongBindingInitDto.setDiagnosisBelongBindingOptions(diagnosisBelongBindingOptions);
        // 诊断验证状态 - 用于医生开诊断"疑似"的取值
        List<DiagnosisBelongBindingInitDto.VerificationStatusOption> verificationStatusOptions =
            Stream.of(ConditionVerificationStatus.values())
                .map(status -> new DiagnosisBelongBindingInitDto.VerificationStatusOption(status.getValue(),
                    status.getInfo()))
                .collect(Collectors.toList());
        diagnosisBelongBindingInitDto.setVerificationStatusOptions(verificationStatusOptions);

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

    /**
     * 查询诊断归属绑定列表
     * 
     * @param diagnosisBelongBindingDto 查询条件dto
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断归属绑定列表
     */
    @GetMapping(value = "/diagnosis-belong-binding-page")
    public R<?> getDiagnosisBelongBindingPage(DiagnosisBelongBindingDto diagnosisBelongBindingDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return iDoctorStationDiagnosisAppService.getDiagnosisBelongBindingPage(diagnosisBelongBindingDto, searchKey,
            pageNo, pageSize);
    }

    /**
     * 删除诊断归属绑定
     * 
     * @param id ID
     * @return 结果
     */
    @DeleteMapping("/diagnosis-belong-binding")
    public R<?> delDiagnosisBelongBinding(@RequestParam Long id) {
        return iDoctorStationDiagnosisAppService.delDiagnosisBelongBinding(id);
    }

    /**
     * 查询诊断定义列表
     * 
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断定义列表
     */
    @GetMapping(value = "/condition-definition-metadata")
    public R<?> getConditionDefinitionMetadata(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R
            .ok(iDoctorStationDiagnosisAppService.getConditionDefinitionMetadataSearchKey(searchKey, pageNo, pageSize));
    }

    /**
     * 医生保存诊断
     * 
     * @param saveDiagnosisParam 诊断信息
     * @return 结果
     */
    @PostMapping(value = "/save-doctor-diagnosis")
    public R<?> saveDoctorDiagnosis(@RequestBody SaveDiagnosisParam saveDiagnosisParam) {
        return iDoctorStationDiagnosisAppService.saveDoctorDiagnosis(saveDiagnosisParam);
    }

}
