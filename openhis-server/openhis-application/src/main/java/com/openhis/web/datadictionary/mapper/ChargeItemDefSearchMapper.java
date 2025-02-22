package com.openhis.web.datadictionary.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.web.datadictionary.dto.ChargeItemDefPageDto;
import com.openhis.web.datadictionary.dto.ItemDefSearchParam;

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
     * @param itemDefSearchParam 查询条件
     * @param pageNo 页码
     * @param pageSize 页面大小
     * @param skipCount 跳过条数
     * @return 分页查询
     */
    List<ChargeItemDefPageDto> getMedList(@Param("itemDefSearchParam") ItemDefSearchParam itemDefSearchParam,
        @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, @Param("skipCount") Integer skipCount);

    /**
     * 器具费用定价分页查询
     * 
     * @param itemDefSearchParam 查询条件
     * @param pageNo 页码
     * @param pageSize 页面大小
     * @param skipCount 跳过条数
     * @return 分页查询
     */
    List<ChargeItemDefPageDto> getDevList(@Param("itemDefSearchParam") ItemDefSearchParam itemDefSearchParam,
        @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, @Param("skipCount") int skipCount);

    /**
     * 活动费用定价分页查询
     *
     * @param itemDefSearchParam 查询条件
     * @param pageNo 页码
     * @param pageSize 页面大小
     * @param skipCount 跳过条数
     * @return 分页查询
     */
    List<ChargeItemDefPageDto> getActList(@Param("itemDefSearchParam") ItemDefSearchParam itemDefSearchParam,
        @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, @Param("skipCount") int skipCount);
}