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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.mapper.PractitionerRoleMapper;
import com.openhis.administration.service.IPractitionerRoleService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
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

    private final IPractitionerRoleService practitionerRoleService;

    @Autowired
    private PractitionerRoleMapper practitionerRoleMapper;

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

        // 构建查询条件
        QueryWrapper<PractitionerRole> queryWrapper = HisQueryUtils.buildQueryWrapper(practRoleSearchParam, searchKey,
            new HashSet<>(Arrays.asList("name")), request);

        // 设置排序
        queryWrapper.orderByDesc("create_time");

        // 执行分页查询并转换为 practitionerRoleDtoPage
        Page<PractitionerRoleDto> practitionerRoleDtoPage =
            HisPageUtils.selectPage(practitionerRoleMapper, queryWrapper, pageNo, pageSize, PractitionerRoleDto.class);

        return R.ok(practitionerRoleDtoPage,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"机构信息"}));

    }

    // /**
    // * 添加岗位信息
    // *
    // * @param practitionerRoleDto 岗位信息
    // */
    // @PostMapping("/practitioner-role")
    // public R<?> addPractitionerRole(@Validated @RequestBody CreatePractitionerRoleDto practitionerRoleDto) {
    //
    // // 新增practitionerRole信息
    // PractitionerRole practitionerRole = new PractitionerRole();
    // BeanUtils.copyProperties(practitionerRoleDto, practitionerRole);
    //
    // boolean savePractitionerRoleSuccess = practitionerRoleService.save(practitionerRole);
    //
    // return savePractitionerRoleSuccess
    // ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"岗位信息"}))
    // : R.fail(PromptMsgConstant.Common.M00007, null);
    // }

    /**
     * 获取岗位需要编辑的信息
     *
     * @param proleId 岗位信息
     */
    @GetMapping("/practitioner-role-getById")
    public R<?> getPractitionerRoleById(@Validated @RequestParam Long proleId) {

        PractitionerRole practitionerRole = practitionerRoleService.getById(proleId);
        return R.ok(practitionerRole,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"岗位信息"}));
    }

    /**
     * 编辑岗位信息
     *
     * @param practitionerRoleDto 岗位信息
     */
    @PutMapping("/practitioner-role")
    public R<?> editPractitionerRole(@Validated @RequestBody PractitionerRoleDto practitionerRoleDto) {

        // 编辑practitionerRole信息
        PractitionerRole practitionerRole = new PractitionerRole();
        BeanUtils.copyProperties(practitionerRoleDto, practitionerRole);
        if (practitionerRole.getRoleCode() == null) {
            return R.fail(PromptMsgConstant.Common.M00007, "角色编码不能为空");
        }
        if (practitionerRole.getOrgId() == null) {
            return R.fail(PromptMsgConstant.Common.M00007, "科室不能为空");
        }
        if (practitionerRole.getLocationId() == null) {
            return R.fail(PromptMsgConstant.Common.M00007, "位置不能为空");
        }

        boolean editPractitionerRoleSuccess = practitionerRoleService.updateById(practitionerRole);

        return editPractitionerRoleSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"岗位信息"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 删除岗位信息
     *
     * @param prId 主表id
     */
    @DeleteMapping("/practitioner-role")
    public R<?> deletePractitionerRole(@RequestParam Long prId) {

        boolean deletePractitionerRoleSuccess = practitionerRoleService.removeById(prId);

        return deletePractitionerRoleSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"岗位信息"}))
            : R.fail(PromptMsgConstant.Common.M00006, null);
    }

}
