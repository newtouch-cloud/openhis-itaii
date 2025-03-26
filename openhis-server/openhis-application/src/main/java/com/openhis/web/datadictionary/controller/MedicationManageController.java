package com.openhis.web.datadictionary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.appservice.IMedicationManageAppService;
import com.openhis.web.datadictionary.dto.MedicationManageUpDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:药品目录
 *
 * @author lpt
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/data-dictionary/medication")
@Slf4j
@AllArgsConstructor
public class MedicationManageController {

    @Autowired
    private IMedicationManageAppService medicationManageAppService;

    /**
     * 药品目录初始化
     *
     * @return
     */
    @GetMapping("/information-init")
    public R<?> getMedicationInit() {
        return medicationManageAppService.getMedicationInit();
    }

    /**
     * 查询药品目录分页列表
     *
     * @param searchKey 查询条件
     * @param statusEnum 查询条件-状态
     * @param ybMatchFlag 查询条件-是否对码
     * @param categoryCode 查询条件-药品分类
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getMedicationList(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "ybMatchFlag", defaultValue = "-1") Integer ybMatchFlag,
        @RequestParam(value = "statusEnum", defaultValue = "-1") Integer statusEnum,
        @RequestParam(value = "categoryCode", defaultValue = "") String categoryCode,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return medicationManageAppService.getMedicationList(searchKey, ybMatchFlag, statusEnum, categoryCode, pageNo,
            pageSize, request);
    }

    /**
     * 根据id查询药品详情
     *
     * @param id 药品ID
     * @return
     */
    @GetMapping("/information-one")
    public R<?> getMedicationOne(@RequestParam Long id) {
        return medicationManageAppService.getMedicationOne(id);
    }

    // 药品目录编辑
    @PutMapping("/information")
    public R<?> editMedication(@RequestBody MedicationManageUpDto medicationManageUpDto) {
        return medicationManageAppService.editMedication(medicationManageUpDto);
    }

    /**
     * 药品目录停用
     *
     * @param ids 药品ID列表
     * @return
     */
    @PutMapping("/information-stop")
    public R<?> editMedicationStop(@RequestBody List<Long> ids) {
        return medicationManageAppService.editMedicationStop(ids);
    }

    /**
     * 药品目录启用
     *
     * @param ids 药品ID列表
     * @return
     */
    @PutMapping("/information-start")
    public R<?> editMedicationStart(@RequestBody List<Long> ids) {
        return medicationManageAppService.editMedicationStart(ids);
    }

    /**
     * 新增外来药品目录
     *
     * @param medicationManageUpDto 药品目录信息
     * @return
     */
    @PostMapping("/information")
    public R<?> addMedication(@Validated @RequestBody MedicationManageUpDto medicationManageUpDto) {
        return medicationManageAppService.addMedication(medicationManageUpDto);
    }

    /**
     * 新增医保药品目录
     *
     * @param medicationManageUpDto 药品目录信息
     * @return
     */
    @PostMapping("/information-yb")
    public R<?> addYbMedication(@RequestBody MedicationManageUpDto medicationManageUpDto) {
        return null;
    }

    /**
     * 药品目录导出
     * 
     * @param searchKey 查询条件
     * @param statusEnum 查询条件-状态
     * @param ybMatchFlag 查询条件-是否对码
     * @param categoryCode 查询条件-药品分类
     * @param response
     * @return
     */
    @GetMapping("/information-export")
    public R<?> exportMedication(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "ybMatchFlag", defaultValue = "-1") Integer ybMatchFlag,
        @RequestParam(value = "statusEnum", defaultValue = "-1") Integer statusEnum,
        @RequestParam(value = "categoryCode", defaultValue = "") String categoryCode, HttpServletResponse response) {
        return medicationManageAppService.exportMedication(searchKey, ybMatchFlag, statusEnum, categoryCode, response);
    }
}
