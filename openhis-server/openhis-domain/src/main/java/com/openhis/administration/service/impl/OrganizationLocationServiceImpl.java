package com.openhis.administration.service.impl;

import java.sql.Time;

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
     * 查询药品和耗材发放的科室
     *
     * @param orgId 机构id
     * @param categoryCode 发放类型
     * @return 药品和耗材发放的科室
     */
    @Override
    public OrganizationLocation getOrgLocByOrgIdAndCategoryCode(Long orgId, String categoryCode) {
        Time time = new Time(System.currentTimeMillis());
        return baseMapper
            .selectOne(new LambdaQueryWrapper<OrganizationLocation>().eq(OrganizationLocation::getOrganizationId, orgId)
                .eq(OrganizationLocation::getDistributionCategoryCode, categoryCode)
                .lt(OrganizationLocation::getStartTime, time).gt(OrganizationLocation::getEndTime, time));
    }

    /**
     * 查询诊疗的执行科室
     *
     * @param activityDefinitionId 诊疗定义id
     * @return 诊疗的执行科室
     */
    @Override
    public OrganizationLocation getOrgLocByOrgIdAndActivityDefinitionId(Long activityDefinitionId) {
        Time time = new Time(System.currentTimeMillis());
        return baseMapper.selectOne(new LambdaQueryWrapper<OrganizationLocation>()
            .eq(OrganizationLocation::getActivityDefinitionId, activityDefinitionId)
            .lt(OrganizationLocation::getStartTime, time).gt(OrganizationLocation::getEndTime, time));
    }

}