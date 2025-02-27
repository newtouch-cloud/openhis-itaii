package com.openhis.web.datadictionary.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.web.datadictionary.dto.ChargeItemDefPageDto;

/**
 * 费用定价管理Mapper接口
 *
 * @author system
 * @date 2025-02-20
 */
@Repository
public interface ChargeItemDefSearchMapper extends BaseMapper<ChargeItemDefinition> {

    /**
     * 药品费用定价分页查询
     *
     * @param page 分页条件
     * @param queryWrapper 查询条件
     * @return 分页查询
     */
    IPage<ChargeItemDefPageDto> getMedList(@Param("page") Page<ChargeItemDefPageDto> page,
        @Param(Constants.WRAPPER) LambdaQueryWrapper<ChargeItemDefPageDto> queryWrapper);

    /**
     * 器具费用定价分页查询
     *
     * @param page 分页条件
     * @param queryWrapper 查询条件
     * @return 分页查询
     */
    IPage<ChargeItemDefPageDto> getDevList(@Param("page") Page<ChargeItemDefPageDto> page,
        @Param(Constants.WRAPPER) LambdaQueryWrapper<ChargeItemDefPageDto> queryWrapper);

    /**
     * 活动费用定价分页查询
     *
     * @param page 分页条件
     * @param queryWrapper 查询条件
     * @return 分页查询
     */
    IPage<ChargeItemDefPageDto> getActList(@Param("page") Page<ChargeItemDefPageDto> page,
        @Param(Constants.WRAPPER) LambdaQueryWrapper<ChargeItemDefPageDto> queryWrapper);
}