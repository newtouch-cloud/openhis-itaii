/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.domain.OrganizationLocation;
import com.openhis.administration.mapper.OrganizationLocationMapper;
import com.openhis.administration.service.ILocationService;
import com.openhis.administration.service.IOrganizationLocationService;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
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
    private final IOrganizationLocationService organizationLocationService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ILocationService locationService;

    @Autowired
    private OrganizationLocationMapper organizationLocationMapper;

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

        // 构建查询条件
        QueryWrapper<OrganizationLocation> queryWrapper = HisQueryUtils.buildQueryWrapper(orgLocQueryParam, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), request);

        // 设置排序
        queryWrapper.orderByDesc("create_time");

        // 执行分页查询并转换为 orgLocQueryDtoPage
        Page<OrgLocQueryDto> orgLocQueryDtoPage =
            HisPageUtils.selectPage(organizationLocationMapper, queryWrapper, pageNo, pageSize, OrgLocQueryDto.class);

        return R.ok(orgLocQueryDtoPage,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"机构信息"}));

    }

    /**
     * 添加机构位置关系信息
     *
     * @param orgLocQueryDto 机构位置关系信息
     */
    @PostMapping("/org-loc")
    public R<?> addOrgLoc(@Validated @RequestBody OrgLocQueryDto orgLocQueryDto) {

        // 新增organizationLocation信息
        OrganizationLocation orgLoc = new OrganizationLocation();
        BeanUtils.copyProperties(orgLocQueryDto, orgLoc);

        boolean saveOrgLocSuccess = organizationLocationService.save(orgLoc);

        return saveOrgLocSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"机构位置关系信息"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 获取机构位置关系需要编辑的信息
     *
     * @param orgLocId 机构位置关系信息
     */
    @GetMapping("/org-loc-getById")
    public R<?> getOrgLocById(@Validated @RequestParam Long orgLocId) {

        OrganizationLocation orgLoc = organizationLocationService.getById(orgLocId);
        return R.ok(orgLoc, MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"机构位置关系信息"}));
    }

    /**
     * 编辑机构位置关系信息
     *
     * @param orgLocQueryDto 机构位置关系信息
     */
    @PutMapping("/org-loc")
    public R<?> editOrgLoc(@Validated @RequestBody OrgLocQueryDto orgLocQueryDto) {

        // 编辑organizationLocation信息
        OrganizationLocation orgLoc = new OrganizationLocation();
        BeanUtils.copyProperties(orgLocQueryDto, orgLoc);

        boolean editOrgLocSuccess = organizationLocationService.updateById(orgLoc);
        return editOrgLocSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"机构位置关系信息"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 删除机构位置关系信息
     *
     * @param orgLocId 主表id
     */
    @DeleteMapping("/org-loc")
    public R<?> delOrgLoc(@RequestParam Long orgLocId) {

        boolean delOrgLocSuccess = organizationLocationService.removeById(orgLocId);

        return delOrgLocSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"机构位置关系信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00006, new Object[] {"机构位置关系信息"}));
    }

}
