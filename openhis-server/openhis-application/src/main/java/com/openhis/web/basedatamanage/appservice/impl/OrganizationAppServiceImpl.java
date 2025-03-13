package com.openhis.web.basedatamanage.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.basedatamanage.appservice.IOrganizationAppService;
import com.openhis.web.basedatamanage.dto.OrganizationQueryDto;

@Service
public class OrganizationAppServiceImpl implements IOrganizationAppService {

    @Resource
    IOrganizationService organizationService;

    @Override
    public Page<OrganizationQueryDto> getOrganizationTree(Integer pageNo, Integer pageSize,
        HttpServletRequest request) {
        // 查询机构列表
        Page<Organization> page = organizationService.page(new Page<>(pageNo, pageSize));
        List<Organization> organizationList = page.getRecords();
        // 将机构列表转为树结构
        List<OrganizationQueryDto> orgTree = buildTree(organizationList);
        Page<OrganizationQueryDto> orgQueryDtoPage = new Page<>(pageNo, pageSize, page.getTotal());
        orgQueryDtoPage.setRecords(orgTree);
        return orgQueryDtoPage;
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

    /**
     * 机构信息详情
     *
     * @param orgId 机构信息id
     * @return 机构信息详情
     */
    @Override
    public R<?> getOrgInfo(Long orgId) {
        Organization organization = organizationService.getById(orgId);
        return R.ok(organization, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"机构信息查询"}));
    }

    /**
     * 添加/编辑机构
     *
     * @param organizationQueryDto 机构信息
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditOrganization(OrganizationQueryDto organizationQueryDto) {

        // 新增organization信息
        Organization organization = new Organization();
        BeanUtils.copyProperties(organizationQueryDto, organization);

        if (organizationQueryDto.getId() != null) {
            // 更新机构信息
            organizationService.updateById(organization);
        } else {
            // 生成待发送的机构信息
            organizationService.save(organization);
        }
        // 返回机构id
        return R.ok(organization.getId(), MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"机构信息更新添加"}));
    }

    /**
     * 删除机构
     *
     * @param orgIds 机构信息id
     * @return 操作结果
     */
    @Override
    public R<?> deleteOrganization(String orgIds) {

        List<Long> orgIdList = new ArrayList<>();
        if (orgIds != null) {
            orgIdList = Arrays.stream(orgIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
        }

        // 删除机构信息
        boolean deleteOrgSuccess = organizationService.removeByIds(orgIdList);
        return deleteOrgSuccess ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"机构信息"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"机构信息"}));
    }

    /**
     * 机构启用
     *
     * @param orgId 机构信息id
     * @return 操作结果
     */
    @Override
    public R<?> activeOrg(Long orgId) {
        // 机构启用
        boolean result = organizationService.activeOrg(orgId);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"机构信息启用"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"机构信息启用"}));
    }

    /**
     * 机构停用
     *
     * @param orgId 机构信息id
     * @return 操作结果
     */
    @Override
    public R<?> inactiveOrg(Long orgId) {
        // 机构停用
        boolean result = organizationService.inactiveOrg(orgId);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"机构信息停用"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"机构信息停用"}));
    }

}
