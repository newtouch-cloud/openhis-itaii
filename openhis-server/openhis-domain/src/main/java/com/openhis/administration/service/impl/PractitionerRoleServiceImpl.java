package com.openhis.administration.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.mapper.PractitionerRoleMapper;
import com.openhis.administration.service.IPractitionerRoleService;
import com.openhis.common.enums.PractitionerRoles;

/**
 * 岗位管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PractitionerRoleServiceImpl extends ServiceImpl<PractitionerRoleMapper, PractitionerRole>
    implements IPractitionerRoleService {

    @Autowired
    PractitionerRoleMapper practitionerRoleMapper;

    /**
     * 根据执行人ID查询
     *
     * @param practitionerId 执行人ID
     * @return 岗位管理实体
     */
    @Override
    public PractitionerRole getPractitionerRoleById(long practitionerId) {
        QueryWrapper<PractitionerRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("practitioner_id", practitionerId);
        return practitionerRoleMapper.selectOne(queryWrapper);
    }

    /**
     * 根据参与者Id，查询其权限下所有科室id
     *
     * @param practitionerId 参与者Id
     * @return 科室ID的集合
     */
    @Override
    public List<Long> getOrgIdsByPractitionerId(Long practitionerId) {
        // 创建查询条件
        LambdaQueryWrapper<PractitionerRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PractitionerRole::getPractitionerId, practitionerId); // 条件：practitionerId 等于指定值

        // 使用 selectList 方法查询所有符合条件的 PractitionerRole 对象
        List<PractitionerRole> practitionerRoleList = practitionerRoleMapper.selectList(queryWrapper);

        // 使用 Stream 提取 orgId 字段
        List<Long> orgIds = practitionerRoleList.stream().map(PractitionerRole::getOrgId) // 提取 orgId 字段
            .collect(Collectors.toList()); // 收集到 List 中
        // 返回科室ID的集合
        return orgIds;
    }

    /**
     * 根据参与者Id，查询其权限下所有位置id
     *
     * @param practitionerId 参与者Id
     * @return 位置ID的集合
     */
    @Override
    public List<Long> getLocationIdsByPractitionerId(Long practitionerId) {
        // 创建查询条件
        LambdaQueryWrapper<PractitionerRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PractitionerRole::getPractitionerId, practitionerId); // 条件：practitionerId 等于指定值

        // 使用 selectList 方法查询所有符合条件的 PractitionerRole 对象
        List<PractitionerRole> practitionerRoleList = practitionerRoleMapper.selectList(queryWrapper);

        // 使用 Stream 提取 locationId 字段
        List<Long> locationIds = practitionerRoleList.stream().map(PractitionerRole::getLocationId) // 提取 locationId 字段
                .collect(Collectors.toList()); // 收集到 List 中
        // 返回位置ID的集合
        return locationIds;
    }

    /**
     * 根据科室Id，查询医生列表
     *
     * @return 医生列表
     */
    @Override
    public List<PractitionerRole> getDoctorList(Long orgId) {

        return baseMapper.selectList(new LambdaQueryWrapper<PractitionerRole>()
            .select(PractitionerRole::getPractitionerId, PractitionerRole::getName)
            .eq(PractitionerRole::getOrgId, orgId)
            // 身份类型是医生
            .eq(PractitionerRole::getRoleCode, PractitionerRoles.DOCTOR.getCode()));

    }

    /**
     * 根据科室Id，查询护士列表
     *
     * @return 护士列表
     */
    @Override
    public List<PractitionerRole> getNurseList(Long orgId) {

        return baseMapper.selectList(new LambdaQueryWrapper<PractitionerRole>()
            .select(PractitionerRole::getPractitionerId, PractitionerRole::getName)
            .eq(PractitionerRole::getOrgId, orgId)
            // 身份类型是护士
            .eq(PractitionerRole::getRoleCode, PractitionerRoles.NURSE.getCode()));

    }
}
