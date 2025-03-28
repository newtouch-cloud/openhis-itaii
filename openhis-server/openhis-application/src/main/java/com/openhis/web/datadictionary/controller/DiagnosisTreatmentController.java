package com.openhis.web.datadictionary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.appservice.IDiagnosisTreatmentManageAppService;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentDto;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentSelParam;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentUpDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:诊疗目录
 *
 * @author lpt
 * @date 2025-02-20
 */
@RestController
@RequestMapping("/data-dictionary/diagnosis-treatment")
@Slf4j
@AllArgsConstructor
public class DiagnosisTreatmentController {

    @Autowired
    private IDiagnosisTreatmentManageAppService diagnosisTreatmentManageAppService;

    /**
     * 诊疗目录初期查询
     *
     * @return
     */
    @GetMapping("/init")
    public R<?> getDiseaseTreatmentInit() {

        return diagnosisTreatmentManageAppService.getDiseaseTreatmentInit();
    }

    /**
     * 查询诊疗目录分页列表
     *
     * @param DiagnosisTreatmentSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getDiseaseTreatmentPage(DiagnosisTreatmentSelParam DiagnosisTreatmentSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 返回【诊疗目录列表DTO】分页
        return diagnosisTreatmentManageAppService.getDiseaseTreatmentPage(DiagnosisTreatmentSelParam, searchKey, pageNo,
            pageSize, request);
    }

    /**
     * 根据id查询诊疗详情
     *
     * @param id 诊疗ID
     * @return
     */
    @GetMapping("/information-one")
    public R<?> getDiseaseTreatmentOne(@RequestParam Long id) {

        return diagnosisTreatmentManageAppService.getDiseaseTreatmentOne(id);
    }

    /**
     * 诊疗目录编辑
     *
     * @param diagnosisTreatmentUpDto 诊疗目录列表
     * @return
     */
    @PutMapping("/information")
    public R<?> editDiseaseTreatment(@RequestBody DiagnosisTreatmentUpDto diagnosisTreatmentUpDto) {

        return diagnosisTreatmentManageAppService.editDiseaseTreatment(diagnosisTreatmentUpDto);
    }

    /**
     * 诊疗目录停用
     *
     * @param ids 诊疗目录ID列表
     * @return
     */
    @PutMapping("/information-stop")
    public R<?> editDiseaseTreatmentStop(@RequestBody List<Long> ids) {

        return diagnosisTreatmentManageAppService.editDiseaseTreatmentStop(ids);
    }

    /**
     * 诊疗目录启用
     *
     * @param ids 诊疗目录ID列表
     * @return
     */
    @PutMapping("/information-start")
    public R<?> editDiseaseTreatmentStart(@RequestBody List<Long> ids) {
        return diagnosisTreatmentManageAppService.editDiseaseTreatmentStart(ids);
    }

    /**
     * 新增外来诊疗目录
     *
     * @param diagnosisTreatmentUpDto 诊疗目录
     * @return
     */
    @PostMapping("/information")
    public R<?> addDiseaseTreatment(@Validated @RequestBody DiagnosisTreatmentUpDto diagnosisTreatmentUpDto) {
        return diagnosisTreatmentManageAppService.addDiseaseTreatment(diagnosisTreatmentUpDto);
    }

    /**
     * 新增医保诊疗目录
     *
     * @param diagnosisTreatmentUpDto 诊疗目录
     * @return
     */
    @PostMapping("/information-yb")
    public R<?> addYbDiseaseTreatment(@RequestBody DiagnosisTreatmentUpDto diagnosisTreatmentUpDto) {
        return null;
    }

    /**
     * 诊疗目录导出
     *
     * @param diagnosisTreatmentDto 诊疗目录
     * @return
     */
    @GetMapping("/information-export")
    public R<?> exportDiseaseTreatment(@RequestBody DiagnosisTreatmentDto diagnosisTreatmentDto) {
        return null;
    }
}
