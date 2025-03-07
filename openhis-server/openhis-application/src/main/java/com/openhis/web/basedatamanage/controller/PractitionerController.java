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
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.mapper.PractitionerMapper;
import com.openhis.administration.mapper.PractitionerRoleMapper;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AccountStatus;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
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

    private final IPractitionerService practitionerService;

    @Autowired
    private PractitionerMapper practitionerMapper;

    @Autowired
    private PractitionerRoleMapper practitionerRoleMapper;

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

        // 构建查询条件
        QueryWrapper<Practitioner> queryWrapper = HisQueryUtils.buildQueryWrapper(practSearchParam, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), request);

        // 设置排序
        queryWrapper.orderByDesc("name");

        // 执行分页查询并转换为 practitionerDtoPage
        Page<PractitionerDto> practitionerDtoPage =
            HisPageUtils.selectPage(practitionerMapper, queryWrapper, pageNo, pageSize, PractitionerDto.class);

        practitionerDtoPage.getRecords().forEach(e -> {
            // 性别回显赋值
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
        });

        return R.ok(practitionerDtoPage,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"员工信息"}));

    }

    /**
     * 添加员工信息
     *
     * @param practitionerDto 员工信息
     */
    @PostMapping("/practitioner")
    public R<?> addPractitioner(@Validated @RequestBody PractitionerDto practitionerDto) {

        // 新增practitioner信息
        Practitioner practitioner = new Practitioner();
        BeanUtils.copyProperties(practitionerDto, practitioner);
        practitioner.setActiveFlag(AccountStatus.ACTIVE.getValue());

        boolean savePractitionerSuccess = practitionerService.save(practitioner);

        PractitionerRole practitionerRole = new PractitionerRole();
        practitionerRole.setPractitionerId(practitioner.getId());
        practitionerRole.setName(practitioner.getName());
        Integer saveProleSuccess = practitionerRoleMapper.insert(practitionerRole);

        boolean saveFlag;
        if (savePractitionerSuccess && saveProleSuccess == 1) {
            saveFlag = true;
        } else {
            saveFlag = false;
        }

        return saveFlag ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"员工信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"员工信息"}));
    }

    /**
     * 获取员工需要编辑的信息
     *
     * @param practitionerId 员工信息
     */
    @GetMapping("/practitioner-getById")
    public R<?> getPractitionerById(@Validated @RequestParam Long practitionerId) {

        Practitioner practitioner = practitionerService.getById(practitionerId);
        if (practitioner != null) {
            return R.ok(practitioner,
                MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"员工信息"}));
        } else {
            return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"员工信息查新失败"}));
        }
    }

    /**
     * 编辑员工信息
     *
     * @param practitionerDto 员工信息
     */
    @PutMapping("/practitioner")
    public R<?> editPractitioner(@Validated @RequestBody PractitionerDto practitionerDto) {

        // 编辑practitioner信息
        Practitioner practitioner = new Practitioner();
        BeanUtils.copyProperties(practitionerDto, practitioner);

        boolean editPractitionerSuccess = practitionerService.updateById(practitioner);

        return editPractitionerSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"员工信息"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 删除员工信息
     *
     * @param practitionerId 主表id
     */
    @DeleteMapping("/practitioner")
    public R<?> deletePractitioner(@RequestParam Long practitionerId) {

        boolean delPractitionerSuccess = practitionerService.removeById(practitionerId);

        return delPractitionerSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"员工信息"}))
            : R.fail(PromptMsgConstant.Common.M00006, null);
    }

}
