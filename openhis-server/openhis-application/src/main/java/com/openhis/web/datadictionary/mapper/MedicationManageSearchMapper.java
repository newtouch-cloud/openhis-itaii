package com.openhis.web.datadictionary.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
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
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return
     */
    IPage<MedicationManageDto> getPage(
        @Param("page") Page<MedicationManageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<MedicationManageDto> queryWrapper);


    /**
     * 药品详情
     * 
     * @param id 药品ID
     * @param tenantId 租户
     * @return
     */
    MedicationManageDto getOne(@Param("id") Long id, @Param("tenantId") Integer tenantId);

    List<MedicationManageDto> getList(@Param("searchKey") String searchKey, @Param("ybMatchFlag") Integer ybMatchFlag,
                                      @Param("statusEnum") Integer statusEnum, @Param("categoryCode") String categoryCode,@Param("tenantId") Integer tenantId);
}
