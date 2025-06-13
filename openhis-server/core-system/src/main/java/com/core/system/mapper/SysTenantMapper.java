package com.core.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.core.system.domain.SysTenant;

/**
 * 租户信息表Mapper接口
 * 
 * @author system
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {

}
