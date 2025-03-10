package com.openhis.administration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.mapper.PractitionerRoleMapper;
import com.openhis.administration.service.IPractitionerRoleService;

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

}