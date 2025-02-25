package com.openhis.administration.service.impl;

import com.openhis.common.enums.ActiveFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.mapper.OrganizationMapper;
import com.openhis.administration.service.IOrganizationService;

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

//    /**
//     * 查询机构列表
//     *
//     * @param classEnum 机构分类枚举
//     * @param activeFlag 活动标识
//     * @return 机构列表
//     */
//    @Override
//    public Page<Organization> getOrganizationPage(Integer classEnum, Integer activeFlag, Integer pageNo,
//        Integer pageSize) {
//
//        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
//
//        if (classEnum != null) {
//            queryWrapper.eq(Organization::getTypeEnum, classEnum);
//        }
//
//        if (activeFlag != null) {
//            queryWrapper.eq(Organization::getActiveFlag, activeFlag);
//        }
//
//        Page<Organization> organizationPage = organizationMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
//
//        // 返回机构信息列表
//        return organizationPage;
//    }

    @Override
    public boolean changeOrgFlag(Long orgId) {

        if (orgId != null) {
            Organization organization = organizationMapper.selectById(orgId);
            if (organization.getActiveFlag().equals(ActiveFlag.YES.getValue())) {
                organization.setActiveFlag(ActiveFlag.NO.getValue());
                return true;
            } else {
                organization.setActiveFlag(ActiveFlag.YES.getValue());
                return true;
            }
        } else {
            return false;
        }
    }

}