package com.openhis.administration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.mapper.OrganizationMapper;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.enums.AccountStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 获取机构下拉列表
     *
     * @param classEnum 机构分类
     * @return 机构下拉列表
     */
    @Override
    public List<Organization> getList(Integer classEnum) {
        return baseMapper.selectList(new LambdaQueryWrapper<Organization>().select(Organization::getId, Organization::getName)
                .eq(Organization::getClassEnum, classEnum));
    }
}