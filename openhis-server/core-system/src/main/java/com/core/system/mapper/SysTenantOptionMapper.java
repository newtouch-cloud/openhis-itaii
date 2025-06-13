package com.core.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.core.system.domain.SysTenantOption;

/**
 * 租户配置项表Mapper接口
 * 
 * @author system
 */
@Mapper
public interface SysTenantOptionMapper extends BaseMapper<SysTenantOption> {

}
