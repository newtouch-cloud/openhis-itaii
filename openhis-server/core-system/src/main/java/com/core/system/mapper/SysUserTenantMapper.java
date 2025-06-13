package com.core.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.core.system.domain.SysUserTenant;

/**
 * 用户租户绑定表Mapper接口
 * 
 * @author system
 */
@Mapper
public interface SysUserTenantMapper extends BaseMapper<SysUserTenant> {

}
