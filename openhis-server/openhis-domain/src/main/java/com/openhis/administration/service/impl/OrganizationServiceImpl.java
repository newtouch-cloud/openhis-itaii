package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.mapper.OrganizationMapper;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.enums.AccountStatus;

/**
 * 机构管理Service业务层处理
 *
 * @author system
 * @date 2025-02-21
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization>
    implements IOrganizationService {

    /**
     * 机构启用
     *
     * @param orgId 机构信息id
     * @return 操作结果
     */
    @Override
    public boolean activeOrg(Long orgId) {
        int updateCount = baseMapper.update(null, new LambdaUpdateWrapper<Organization>().eq(Organization::getId, orgId)
            .set(Organization::getActiveFlag, AccountStatus.ACTIVE.getValue()));
        return updateCount > 0;
    }

    /**
     * 机构停用
     *
     * @param orgId 机构信息id
     * @return 操作结果
     */
    @Override
    public boolean inactiveOrg(Long orgId) {
        int updateCount = baseMapper.update(null, new LambdaUpdateWrapper<Organization>().eq(Organization::getId, orgId)
            .set(Organization::getActiveFlag, AccountStatus.INACTIVE.getValue()));
        return updateCount > 0;
    }

}