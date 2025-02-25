package com.openhis.web.datadictionary.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.web.datadictionary.dto.MedicationManageDto;

/**
 * 药品目录管理Mapper接口
 *
 * @author lpt
 * @date 2025-02-25
 */
@Repository
public interface MedicationManageSearchMapper extends BaseMapper<ChargeItemDefinition> {
    /**
     * 药品目录分页查询
     * 
     * @param searchKey 模糊查询条件
     * @param ybMatchFlag 是否对码
     * @param statusEnum 状态
     * @param categoryCode 分类
     * @param tenantId 租户
     * @param pageSize
     * @param offset
     * @return
     */
    List<MedicationManageDto> getPage(@Param("searchKey") String searchKey, @Param("ybMatchFlag") Integer ybMatchFlag,
        @Param("statusEnum") Integer statusEnum, @Param("categoryCode") String categoryCode,
        @Param("tenantId") Integer tenantId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    /**
     * 药品目录分页查询
     * 
     * @param searchKey 模糊查询条件
     * @param ybMatchFlag 是否对码
     * @param statusEnum 状态
     * @param categoryCode 分类
     * @param tenantId 租户
     * @return
     */
    Long getPageCount(@Param("searchKey") String searchKey, @Param("ybMatchFlag") Integer ybMatchFlag,
        @Param("statusEnum") Integer statusEnum, @Param("categoryCode") String categoryCode,
        @Param("tenantId") Integer tenantId);

    /**
     * 药品详情
     * 
     * @param id 药品ID
     * @param tenantId 租户
     * @return
     */
    MedicationManageDto getOne(@Param("id") Long id, @Param("tenantId") Integer tenantId);
}
