/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.appservice.IOrganizationLocationAppService;
import com.openhis.web.basedatamanage.dto.OrgLocQueryDto;
import com.openhis.web.basedatamanage.dto.OrgLocQueryParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 机构位置关系controller
 *
 * @author
 * @date 2025-02-25
 */
@RestController
@RequestMapping("/base-data-manage/org-loc")
@Slf4j
@AllArgsConstructor
public class OrganizationLocationController {

    @Autowired
    private IOrganizationLocationAppService organizationLocationAppService;

    /**
     * 机构位置关系初始化
     *
     * @return 操作结果
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        return organizationLocationAppService.organizationLocationInit();
    }

    /**
     * 机构位置关系分页列表
     *
     * @param orgLocQueryParam 查询字段
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 机构位置关系分页列表
     */
    @GetMapping(value = "/org-loc")
    public R<?> getOrgLocPage(OrgLocQueryParam orgLocQueryParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return organizationLocationAppService.getOrgLocPage(orgLocQueryParam, pageNo, pageSize, request);
    }

    /**
     * 根据类型查询药房/药库
     *
     * @param locationForm 查询字段
     * @return 机构位置关系分页列表
     */
    @GetMapping(value = "/loc-list")
    public R<?> getLocationListByForm(@RequestParam Integer locationForm) {
        return organizationLocationAppService.getLocationListByForm(locationForm);
    }

    /**
     * 新增/编辑机构位置关系信息
     *
     * @param orgLocQueryDto 机构位置关系信息
     */
    @PostMapping("/org-loc")
    public R<?> addOrEditOrgLoc(@Validated @RequestBody OrgLocQueryDto orgLocQueryDto) {
        return organizationLocationAppService.addOrEditOrgLoc(orgLocQueryDto);
    }

    /**
     * 删除机构位置关系信息
     *
     * @param orgLocId 主表id
     */
    @DeleteMapping("/org-loc")
    public R<?> delOrgLoc(@RequestParam Long orgLocId) {
        return organizationLocationAppService.deleteOrgLoc(orgLocId);
    }

}
