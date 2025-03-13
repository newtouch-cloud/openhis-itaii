/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.chargemanage.dto.EncounterPatientPageDto;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;

/**
 * 门诊收费 appMapper
 *
 * @author zwh
 * @date 2025-03-13
 */
@Repository
public interface OutpatientChargeAppMapper {

    /**
     * 查询就诊患者分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 就诊患者分页列表
     */
    Page<EncounterPatientPageDto> selectEncounterPatientPage(@Param("page") Page<EncounterPatientPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<EncounterPatientPageParam> queryWrapper);
}
