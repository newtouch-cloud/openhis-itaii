package com.openhis.administration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public boolean changeOrgFlag(Long orgId) {

        if (orgId != null) {
            Organization organization = organizationMapper.selectById(orgId);
            if (organization.getActiveFlag().equals(AccountStatus.ACTIVE.getValue())) {
                organization.setActiveFlag(AccountStatus.INACTIVE.getValue());
                return true;
            } else {
                organization.setActiveFlag(AccountStatus.ACTIVE.getValue());
                return true;
            }
        } else {
            return false;
        }
    }

}