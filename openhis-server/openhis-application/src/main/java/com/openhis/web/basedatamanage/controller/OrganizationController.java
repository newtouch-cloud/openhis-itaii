/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.mapper.OrganizationMapper;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.OrganizationType;
import com.openhis.web.basedatamanage.dto.OrgQueryParam;
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

    private final IOrganizationService organizationService;

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
     * @param orgQueryParam 查询字段
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 机构分页列表
     */
    @GetMapping(value = "/organization")
    public R<?> getOrganizationPage(@RequestBody OrgQueryParam orgQueryParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // // 构建查询条件
        // QueryWrapper<Organization> queryWrapper = HisQueryUtils.buildQueryWrapper(orgQueryParam, searchKey,
        // new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), request);
        //
        // // 设置排序
        // queryWrapper.orderByDesc("create_time");
        // // 执行分页查询并转换为 orgQueryDtoPage
        // Page<OrganizationQueryDto> orgQueryDtoPage =
        // HisPageUtils.selectPage(organizationMapper, queryWrapper, pageNo, pageSize, OrganizationQueryDto.class);
        // 查询机构列表
        Page<Organization> page = organizationService.page(new Page<>(pageNo, pageSize));
        List<Organization> organizationList = page.getRecords();
        // 将机构列表转为树结构
        List<OrganizationQueryDto> orgTree = buildTree(organizationList);
        Page<OrganizationQueryDto> orgQueryDtoPage = new Page<>(pageNo, pageSize, page.getCurrent());
        orgQueryDtoPage.setRecords(orgTree);

        return R.ok(orgQueryDtoPage,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"机构信息"}));
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
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"机构信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"机构信息"}));
    }

    /**
     * 获取机构需要编辑的信息
     *
     * @param orgId 机构信息
     */
    @GetMapping("/organization-editById")
    public R<?> getOrganizationById(@Validated @RequestParam Long orgId) {

        Organization organization = organizationService.getById(orgId);
        return R.ok(organization, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"机构信息"}));
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
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"机构信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"机构信息"}));
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
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"机构信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00006, new Object[] {"机构信息"}));
    }

    /**
     * 停用启用
     *
     * @param orgId 主表id
     */
    @PutMapping("/organization-flag")
    public R<?> changeOrgFlag(@RequestParam Long orgId) {

        boolean flagChangeSuccess = organizationService.changeOrgFlag(orgId);

        return flagChangeSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"机构活动标识"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"机构活动标识"}));
    }

    /**
     * 将机构列表转换为树结构
     *
     * @param records 机构列表
     * @return tree
     */
    private List<OrganizationQueryDto> buildTree(List<Organization> records) {
        // 按b_no的层级排序，确保父节点先处理
        List<Organization> sortedRecords = records.stream()
            .sorted(Comparator.comparingInt(r -> r.getBusNo().split("\\.").length)).collect(Collectors.toList());

        Map<String, OrganizationQueryDto> nodeMap = new HashMap<>();
        List<OrganizationQueryDto> tree = new ArrayList<>();

        for (Organization record : sortedRecords) {
            String bNo = record.getBusNo();
            String[] parts = bNo.split("\\.");
            OrganizationQueryDto node = new OrganizationQueryDto();
            BeanUtils.copyProperties(record, node);
            // 将当前节点加入映射
            nodeMap.put(bNo, node);

            if (parts.length == 1) {
                // 根节点
                tree.add(node);
            } else {
                // 获取父节点的b_no（去掉最后一部分）
                String parentBNo = String.join(".", Arrays.copyOf(parts, parts.length - 1));
                OrganizationQueryDto parent = nodeMap.get(parentBNo);

                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 处理父节点不存在的情况（例如数据缺失）
                    // 可根据需求调整为将节点加入根或抛出异常
                }
            }
        }
        return tree;
    }

}
