package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.OrganizationLocation;

/**
 * 机构位置关系管理Service接口
 *
 * @author system
 * @date 2025-02-25
 */
public interface IOrganizationLocationService extends IService<OrganizationLocation> {

    /**
     * 查询机构位置关系
     * 
     * @param orgId 机构id
     * @param categoryCode 发放类型
     * @return机构位置关系
     */
    OrganizationLocation getOrgLocByOrgIdAndCategoryCode(Long orgId, String categoryCode);

}