/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.OrganizationType;
import com.openhis.web.basedatamanage.appservice.IOrganizationAppService;
import com.openhis.web.basedatamanage.dto.OrganizationInitDto;
import com.openhis.web.basedatamanage.dto.OrganizationQueryDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 机构管理controller
 *
 * @author
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/basedatamanage/organization")
@Slf4j
@AllArgsConstructor
public class OrganizationController {

    private final IOrganizationAppService iOrganizationAppService;

    @GetMapping(value = "/init")
    public R<?> init() {

        List<OrganizationInitDto> initDto = new ArrayList<>();

        for (OrganizationType type : OrganizationType.values()) {
            initDto.add(new OrganizationInitDto(type.getValue(), type.getCode()));
        }
        return R.ok(initDto);
    }

    /**
     * 机构分页列表
     *
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 机构分页列表
     */
    @GetMapping(value = "/organization")
    public R<?> getOrganizationPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        Page<OrganizationQueryDto> organizationTree =
            iOrganizationAppService.getOrganizationTree(pageNo, pageSize, request);
        return R.ok(organizationTree,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"机构信息"}));
    }

    /**
     * 获取机构需要编辑的信息
     *
     * @param orgId 机构信息
     * @return 操作结果
     */
    @GetMapping("/organization-getById")
    public R<?> getOrgInfo(@Validated @RequestParam Long orgId) {
        return iOrganizationAppService.getOrgInfo(orgId);
    }

    /**
     * 添加/编辑机构信息
     *
     * @param organizationQueryDto 机构信息
     * @return 操作结果
     */
    @PutMapping("/organization")
    public R<?> addOrEditInventoryReceipt(@Validated @RequestBody OrganizationQueryDto organizationQueryDto) {
        return iOrganizationAppService.addOrEditOrganization(organizationQueryDto);
    }

    /**
     * 删除机构信息
     *
     * @param orgIds 需要删除的Id
     * @return 操作结果
     */
    @DeleteMapping("/organization")
    public R<?> deleteOrganization(@RequestParam String orgIds) {
        return iOrganizationAppService.deleteOrganization(orgIds);
    }

    /**
     * 机构启用
     * 
     * @param orgId 启用数据的Id
     * @return 操作结果
     *
     */
    @PutMapping("/organization-active")
    public R<?> activeOrganization(@RequestParam Long orgId) {
        return iOrganizationAppService.activeOrg(orgId);
    }

    /**
     * 机构停用
     * 
     * @param orgId 停用数据的Id
     * @return 操作结果
     *
     */
    @PutMapping("/organization-inactive")
    public R<?> inactiveOrganization(@RequestParam Long orgId) {
        return iOrganizationAppService.inactiveOrg(orgId);
    }

}
