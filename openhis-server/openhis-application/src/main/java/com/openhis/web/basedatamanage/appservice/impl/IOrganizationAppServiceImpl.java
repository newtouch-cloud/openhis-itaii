package com.openhis.web.basedatamanage.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.web.basedatamanage.appservice.IOrganizationAppService;
import com.openhis.web.basedatamanage.dto.OrganizationQueryDto;

@Service
public class IOrganizationAppServiceImpl implements IOrganizationAppService {

    @Resource
    IOrganizationService organizationService;

    @Override
    public Page<OrganizationQueryDto> getOrganizationTree(Integer pageNo, Integer pageSize) {
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

}
