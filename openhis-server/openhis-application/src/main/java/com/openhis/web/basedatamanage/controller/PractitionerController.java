/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.appservice.IPractitionerAppService;
import com.openhis.web.basedatamanage.dto.PractSearchParam;
import com.openhis.web.basedatamanage.dto.PractitionerDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 员工管理Controller业务层处理
 *
 * @author
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/basedatamanage/practitioner")
@Slf4j
@AllArgsConstructor
public class PractitionerController {

    @Autowired
    private IPractitionerAppService practitionerAppService;

    /**
     * 员工分页列表
     *
     * @param practSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 员工分页列表
     */
    @GetMapping(value = "/practitioner")
    public R<?> getPractitionerPage(PractSearchParam practSearchParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return practitionerAppService.getPractitionerPage(practSearchParam, searchKey, pageNo, pageSize, request);
    }

    /**
     * 获取员工需要编辑的信息
     *
     * @param practitionerId 员工信息
     */
    @GetMapping("/practitioner-getById")
    public R<?> getPractitionerById(@Validated @RequestParam Long practitionerId) {
        return practitionerAppService.getPractitionerById(practitionerId);
    }

    /**
     * 编辑员工信息
     *
     * @param practitionerDto 员工信息
     */
    @PutMapping("/practitioner")
    public R<?> addOrEditPractitioner(@Validated @RequestBody PractitionerDto practitionerDto) {
        return practitionerAppService.addOrEditPractitioner(practitionerDto);
    }

    /**
     * 删除员工信息
     *
     * @param practitionerId 主表id
     */
    @DeleteMapping("/practitioner")
    public R<?> deletePractitioner(@RequestParam Long practitionerId) {
        return practitionerAppService.deletePractitioner(practitionerId);
    }

}
