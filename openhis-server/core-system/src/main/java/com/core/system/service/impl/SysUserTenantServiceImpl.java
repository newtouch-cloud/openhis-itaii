package com.core.system.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.system.domain.SysUserTenant;
import com.core.system.mapper.SysUserTenantMapper;
import com.core.system.service.ISysUserTenantService;

/**
 * 用户租户绑定表Service业务层处理
 * 
 * @author system
 */
@Service
public class SysUserTenantServiceImpl extends ServiceImpl<SysUserTenantMapper, SysUserTenant>
    implements ISysUserTenantService {

}
