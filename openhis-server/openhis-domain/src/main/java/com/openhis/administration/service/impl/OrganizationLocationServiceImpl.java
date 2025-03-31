package com.openhis.administration.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.OrganizationLocation;
import com.openhis.administration.mapper.OrganizationLocationMapper;
import com.openhis.administration.service.IOrganizationLocationService;

/**
 * 机构位置关系管理Service业务层处理
 *
 * @author system
 * @date 2025-02-25
 */
@Service
public class OrganizationLocationServiceImpl extends ServiceImpl<OrganizationLocationMapper, OrganizationLocation>
    implements IOrganizationLocationService {

    /**
     * 查询机构位置关系
     *
     * @param orgId 机构id
     * @param categoryCode 发放类型
     * @return机构位置关系
     */
    @Override
    public OrganizationLocation getOrgLocByOrgIdAndCategoryCode(Long orgId, String categoryCode) {
        OrganizationLocation organizationLocation = baseMapper
            .selectOne(new LambdaQueryWrapper<OrganizationLocation>().eq(OrganizationLocation::getOrganizationId, orgId)
                .eq(OrganizationLocation::getDistributionCategoryCode, categoryCode)
                .lt(OrganizationLocation::getStartTime, new Date()).gt(OrganizationLocation::getEndTime, new Date()));
        return organizationLocation;
    }

}