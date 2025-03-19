/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.chargemanage.dto.EncounterPatientPageDto;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;
import com.openhis.web.chargemanage.dto.EncounterPatientPrescriptionDto;

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

    /**
     * 根据就诊id查询患者处方列表
     *
     * @param encounterId 就诊id
     * @param activity 项目
     * @param medication 药品
     * @param device 耗材
     * @param planned 收费状态：待收费
     * @param billable 收费状态：待结算
     * @param billed 收费状态：已结算
     * @return 患者处方列表
     */
    List<EncounterPatientPrescriptionDto> selectEncounterPatientPrescription(@Param("encounterId") Long encounterId,
        @Param("activity") Integer activity, @Param("medication") Integer medication, @Param("device") Integer device,
        @Param("planned") Integer planned, @Param("billable") Integer billable, @Param("billed") Integer billed);
}
