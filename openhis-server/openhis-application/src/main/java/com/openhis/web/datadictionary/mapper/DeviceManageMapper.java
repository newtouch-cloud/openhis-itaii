package com.openhis.web.datadictionary.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.datadictionary.dto.DeviceManageDto;
import com.openhis.web.datadictionary.dto.MedicationManageDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import org.apache.ibatis.annotations.Param;

/**
 * 器材目录
 *
 * @author Wuser
 * @date 2025/3/26
 */
public interface DeviceManageMapper {

    /**
     * 器材目录分页查询
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 器材记录
     */
    IPage<DeviceManageDto> getDevicePage(
        @Param("page") Page<DeviceManageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<DeviceManageDto> queryWrapper);

    /**
     * 器材详情
     *
     * @param id 器材ID
     * @param tenantId 租户
     * @return
     */
    DeviceManageDto getOne(@Param("id") Long id, @Param("tenantId") Integer tenantId);

}
