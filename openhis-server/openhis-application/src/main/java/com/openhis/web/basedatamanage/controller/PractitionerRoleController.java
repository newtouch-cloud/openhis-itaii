/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.appservice.IPractitionerRoleAppService;
import com.openhis.web.basedatamanage.dto.PractRoleSearchParam;
import com.openhis.web.basedatamanage.dto.PractitionerRoleDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 岗位管理Controller业务层处理
 *
 * @author
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/basedatamanage/practitioner-role")
@Slf4j
@AllArgsConstructor
public class PractitionerRoleController {

    @Autowired
    private IPractitionerRoleAppService practitionerRoleAppService;

    /**
     * 岗位分页列表
     *
     * @param practRoleSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 岗位分页列表
     */
    @GetMapping(value = "/practitioner-role")
    public R<?> getPractitionerRolePage(PractRoleSearchParam practRoleSearchParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return practitionerRoleAppService.getPractitionerPage(practRoleSearchParam, searchKey, pageNo, pageSize,
            request);
    }

    /**
     * 获取岗位需要编辑的信息
     *
     * @param practitionerRoleId 岗位信息
     */
    @GetMapping("/practitioner-role-getById")
    public R<?> getPractitionerRoleById(@Validated @RequestParam Long practitionerRoleId) {
        return practitionerRoleAppService.getPractitionerRoleById(practitionerRoleId);
    }

    /**
     * 编辑岗位信息
     *
     * @param practitionerRoleDto 岗位信息
     */
    @PutMapping("/practitioner-role")
    public R<?> editPractitionerRole(@Validated @RequestBody PractitionerRoleDto practitionerRoleDto) {
        return practitionerRoleAppService.addOrEditPractitionerRole(practitionerRoleDto);
    }

    /**
     * 删除岗位信息
     *
     * @param practitionerRoleId 主表id
     */
    @DeleteMapping("/practitioner-role")
    public R<?> deletePractitionerRole(@RequestParam Long practitionerRoleId) {
        return practitionerRoleAppService.deletePractitionerRole(practitionerRoleId);
    }
}
