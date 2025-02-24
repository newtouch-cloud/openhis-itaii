/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.enums.AssignSeqEnum;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.PromptMsgConstant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 机构管理(科室)controller
 *
 * @author
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/basedatamanage/organization")
@Slf4j
@AllArgsConstructor
public class OrganizationController {

    private final IOrganizationService organizationService;

    @Autowired(required = false)
    private AssignSeqUtil assignSeqUtil;

    /**
     * 添加科室信息
     *
     * @param organization 科室信息
     */
    @PostMapping("/organization")
    public R<?> addOrganization(@Validated @RequestBody Organization organization) {

        // 新增organization信息
        // Organization organization = new Organization();
        // BeanUtils.copyProperties(organizationdto, organization);

        // 基础采番
        String code = assignSeqUtil.getSeq(AssignSeqEnum.TEST.getPrefix());
        organization.setBus_no(code);

        boolean saveOrganizationSuccess = organizationService.save(organization);

        return saveOrganizationSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"科室信息"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 编辑科室信息
     *
     * @param organization 科室信息
     */
    @PutMapping("/organization")
    public R<?> editOrganization(@Validated @RequestBody Organization organization) {
        // 更新organization信息
        // Organization organization = new Organization();
        // BeanUtils.copyProperties(organizationdto, organization);

        boolean updateSuccess = organizationService.updateById(organization);
        return updateSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"科室信息"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 删除科室信息
     *
     * @param orgId 主表id
     */
    @DeleteMapping("/organization")
    public R<?> deleteSupplyRequest(@RequestParam Long orgId) {

        boolean deleteSuccess = organizationService.removeById(orgId);

        return deleteSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"科室信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00006, new Object[] {"科室信息删除失败"}));
    }

    /**
     * 科室分页列表
     *
     * @param classEnum 机构分类枚举
     * @param activeFlag 活动标识
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 科室分页列表
     */
    @GetMapping(value = "/organization")
    public R<?> getOrganizationPage(Integer classEnum, Integer activeFlag,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 查询【科室】分页列表
        Page<Organization> organizationPage =
            organizationService.getOrganizationPage(classEnum, activeFlag, pageNo, pageSize);
        return R.ok(organizationPage);
    }

}
