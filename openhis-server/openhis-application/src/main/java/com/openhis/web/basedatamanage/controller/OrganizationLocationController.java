/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.core.common.core.domain.R;
import com.openhis.administration.domain.Location;
import com.openhis.administration.service.ILocationService;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.web.basedatamanage.appservice.IOrganizationLocationAppService;
import com.openhis.web.basedatamanage.dto.OrgLocInitDto;
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
@RequestMapping("/basedatamanage/org-loc")
@Slf4j
@AllArgsConstructor
public class OrganizationLocationController {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ILocationService locationService;

    @Autowired
    private IOrganizationLocationAppService iOrganizationLocationAppService;

    /**
     * 机构位置关系初始化
     *
     */
    @GetMapping(value = "/init")
    public R<?> init() {

        OrgLocInitDto initDto = new OrgLocInitDto();
        // 设置科室列表
        initDto.setOrganization(organizationService.list())
            // 设置药库列表
            .setLocation(locationService.list(new LambdaQueryWrapper<Location>().in(Location::getFormEnum, 11)));
        return R.ok(initDto);
    }

    /**
     * 机构位置关系分页列表
     *
     * @param orgLocQueryParam 查询字段
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 机构位置关系分页列表
     */
    @GetMapping(value = "/org-loc")
    public R<?> getOrgLocPage(OrgLocQueryParam orgLocQueryParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return iOrganizationLocationAppService.getOrgLocPage(orgLocQueryParam, searchKey, pageNo, pageSize, request);

    }

    /**
     * 获取机构位置关系需要编辑的信息
     *
     * @param orgLocId 机构位置关系信息
     */
    @GetMapping("/org-loc-getById")
    public R<?> getOrgLocById(@Validated @RequestParam Long orgLocId) {
        return iOrganizationLocationAppService.getOrgLocById(orgLocId);
    }

    /**
     * 编辑机构位置关系信息
     *
     * @param orgLocQueryDto 机构位置关系信息
     */
    @PutMapping("/org-loc")
    public R<?> addOrEditOrgLoc(@Validated @RequestBody OrgLocQueryDto orgLocQueryDto) {
        return iOrganizationLocationAppService.addOrEditOrgLoc(orgLocQueryDto);
    }

    /**
     * 删除机构位置关系信息
     *
     * @param orgLocId 主表id
     */
    @DeleteMapping("/org-loc")
    public R<?> delOrgLoc(@RequestParam Long orgLocId) {
        return iOrganizationLocationAppService.deleteOrgLoc(orgLocId);
    }

}
