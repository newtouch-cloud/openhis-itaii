/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.personalization.dto.ActivityDeviceDto;

/**
 * 诊疗耗材绑定 appMapper
 *
 * @author zwh
 * @date 2025-04-09
 */
@Repository
public interface ActivityDeviceAppMapper {

    /**
     * 诊疗耗材绑定分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 诊疗耗材绑定分页列表
     */
    Page<ActivityDeviceDto> selectActivityDevicePage(@Param("page") Page<ActivityDeviceDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<ActivityDeviceDto> queryWrapper);
}
