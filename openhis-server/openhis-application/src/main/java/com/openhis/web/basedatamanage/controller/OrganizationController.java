/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.mapper.OrganizationMapper;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.OrganizationType;
import com.openhis.web.basedatamanage.appservice.IOrganizationAppService;
import com.openhis.web.basedatamanage.dto.OrganizationInitDto;
import com.openhis.web.basedatamanage.dto.OrganizationQueryDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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

    private final IOrganizationService organizationService;
    private final IOrganizationAppService iOrganizationAppService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired(required = false)
    private AssignSeqUtil assignSeqUtil;

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
     * @param pageNo   当前页码
     * @param pageSize 查询条数
     * @return 机构分页列表
     */
    @GetMapping(value = "/organization")
    public R<?> getOrganizationPage(
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<OrganizationQueryDto> organizationTree = iOrganizationAppService.getOrganizationTree(pageNo, pageSize);
        return R.ok(organizationTree,
                MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[]{"机构信息"}));
    }

    /**
     * 添加机构信息
     *
     * @param organizationQueryDto 机构信息
     */
    @PostMapping("/organization")
    public R<?> addOrganization(@Validated @RequestBody OrganizationQueryDto organizationQueryDto) {

        // 新增organization信息
        Organization organization = new Organization();
        BeanUtils.copyProperties(organizationQueryDto, organization);

        // 基础采番
        // String code = assignSeqUtil.getSeq(AssignSeqEnum.TEST.getPrefix());
        // organizationQueryDto.setBusNo(code);

        boolean saveOrgSuccess = organizationService.save(organization);

        return saveOrgSuccess
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[]{"机构信息"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[]{"机构信息"}));
    }

    /**
     * 获取机构需要编辑的信息
     *
     * @param orgId 机构信息
     */
    @GetMapping("/organization-getById")
    public R<?> getOrganizationById(@Validated @RequestParam Long orgId) {

        Organization organization = organizationService.getById(orgId);
        return R.ok(organization, MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[]{"机构信息"}));
    }

    /**
     * 编辑机构信息
     *
     * @param organizationQueryDto 机构信息
     */
    @PutMapping("/organization")
    public R<?> editOrganization(@Validated @RequestBody OrganizationQueryDto organizationQueryDto) {
        // 更新organization信息
        Organization organization = new Organization();
        BeanUtils.copyProperties(organizationQueryDto, organization);

        boolean updateOrgSuccess = organizationService.updateById(organization);
        return updateOrgSuccess
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[]{"机构信息"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[]{"机构信息"}));
    }

    /**
     * 删除机构信息
     *
     * @param orgIds 需要删除的Id
     */
    @DeleteMapping("/organization")
    public R<?> deleteOrganization(@RequestParam String orgIds) {

        List<Long> orgIdList = new ArrayList<>();
        if (orgIds != null) {
            orgIdList = Arrays.stream(orgIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
        }

        // 在service层做业务校验
        boolean deleteOrgSuccess = organizationService.removeByIds(orgIdList);

        return deleteOrgSuccess
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[]{"机构信息"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00006, new Object[]{"机构信息"}));
    }

    /**
     * 启用
     *
     * @param orgId 启用数据的Id
     */
    @PutMapping("/organization-active")
    public R<?> changeActive(@RequestParam Long orgId) {

        boolean activeSuccess = organizationService.activeChange(orgId);

        return activeSuccess
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[]{"启用"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[]{"启用"}));
    }

    /**
     * 停用
     *
     * @param orgId 停用数据的Id
     */
    @PutMapping("/organization-inactive")
    public R<?> changeInactive(@RequestParam Long orgId) {

        boolean inActiveSuccess = organizationService.activeChange(orgId);

        return inActiveSuccess
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[]{"停用"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[]{"停用"}));
    }


}
