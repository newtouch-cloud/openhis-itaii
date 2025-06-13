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
     * 查询药品和耗材发放的科室
     * 
     * @param orgId 机构id
     * @param categoryCode 发放类型
     * @return 药品和耗材发放的科室
     */
    OrganizationLocation getOrgLocByOrgIdAndCategoryCode(Long orgId, String categoryCode);

    /**
     * 查询诊疗的执行科室
     * 
     * @param activityDefinitionId 诊疗定义id
     * @return 诊疗的执行科室
     */
    OrganizationLocation getOrgLocByOrgIdAndActivityDefinitionId(Long activityDefinitionId);

}