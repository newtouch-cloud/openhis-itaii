package com.openhis.web.basedatamanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.dto.OrganizationQueryDto;

/**
 * Organization 应该服务类
 */
public interface IOrganizationAppService {
    /**
     * 查询机构树
     *
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 机构树分页列表
     */
    Page<OrganizationQueryDto> getOrganizationTree(Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 机构信息详情
     *
     * @param orgId 机构信息id
     * @return 机构信息详情
     */
    R<?> getOrgInfo(Long orgId);

    /**
     * 添加/编辑机构信息
     *
     * @param organizationQueryDto 机构信息
     * @return 操作结果
     */
    R<?> addOrEditOrganization(OrganizationQueryDto organizationQueryDto);

    /**
     * 机构信息
     *
     * @param orgIds 机构信息id
     * @return 操作结果
     */
    R<?> deleteOrganization(String orgIds);

    /**
     * 机构启用
     *
     * @param orgId 机构信息id
     * @return 操作结果
     */
    R<?> activeOrg(Long orgId);

    /**
     * 机构停用
     *
     * @param orgId 机构信息id
     * @return 操作结果
     */
    R<?> inactiveOrg(Long orgId);

}
