/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.pharmacymanage.dto.PendingMedicationPageDto;
import com.openhis.web.pharmacymanage.dto.PendingMedicationSearchParam;

@Repository
public interface PendingMedicationDetailsMapper {

    /**
     * 分页查询待发药明细
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param inProgress 发药类型：待发药
     * @return 待发药明细
     */
    Page<PendingMedicationPageDto> selectPendingMedicationDetailsPage(
        @Param("page") Page<PendingMedicationPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<PendingMedicationSearchParam> queryWrapper,
        @Param("inProgress") Integer inProgress, @Param("amb") Integer amb, @Param("imp") Integer imp);

}
