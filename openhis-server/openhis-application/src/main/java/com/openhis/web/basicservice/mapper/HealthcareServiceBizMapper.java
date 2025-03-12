package com.openhis.web.basicservice.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.basicservice.dto.HealthcareServiceDto;

/**
 * 服务管理 自定义Mapper
 */
@Repository
public interface HealthcareServiceBizMapper {

    /**
     * 服务管理 分页查询
     *
     * @param page 分页参数
     * @param tableName 定价表名
     * @param queryWrapper 查询条件
     * @return 列表信息
     */
    IPage<HealthcareServiceDto> getHealthcareServicePage(@Param("page") Page<HealthcareServiceDto> page,
        @Param("tableName") String tableName,
        @Param(Constants.WRAPPER) QueryWrapper<HealthcareServiceDto> queryWrapper);

}
