package com.openhis.web.datadictionary.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentDto;

/**
 * 诊疗定义管理
 *
 * @author liuhr
 * @date 2025/3/28
 */
@Repository
public interface ActivityDefinitionManageMapper {

    /**
     * 诊疗目录分页查询
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 器材记录
     */
    IPage<DiagnosisTreatmentDto> getDiseaseTreatmentPage(@Param("page") Page<DiagnosisTreatmentDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<DiagnosisTreatmentDto> queryWrapper);

    /**
     * 诊疗详情
     *
     * @param id 诊疗ID
     * @param tenantId 租户
     * @return
     */
    DiagnosisTreatmentDto getDiseaseTreatmentOne(@Param("id") Long id, @Param("tenantId") Integer tenantId);

}
