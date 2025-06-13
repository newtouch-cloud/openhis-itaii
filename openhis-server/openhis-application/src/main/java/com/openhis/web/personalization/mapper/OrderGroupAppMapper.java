/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.personalization.dto.OrderGroupDto;

/**
 * 组套 Mapper
 *
 * @author zwh
 * @date 2025-05-16
 */
@Repository
public interface OrderGroupAppMapper {

    /**
     * 组套分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 组套分页列表
     */
    Page<OrderGroupDto> selectOrderGroup(@Param("page") Page<OrderGroupDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<OrderGroupDto> queryWrapper);
}
