/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.pharmacymanage.dto.MedDetailedAccountPageDto;
import com.openhis.web.pharmacymanage.dto.MedDetailsSearchParam;
import com.openhis.web.pharmacymanage.dto.MedRunningAccountPageDto;

@Repository
public interface MedicationDetailsMapper {

    /**
     * 门诊人员发药明细表/门诊发药明细流水账
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param amb 发药类型：门诊
     * @param completed 发药状态：已发药
     * @param refunded 发药状态：已退药
     * @param dispenseEnum 统计类型
     * @return 门诊人员发药明细表
     */
    Page<MedDetailedAccountPageDto> selectAmbPractitionerDetailPage(@Param("page") Page<MedDetailedAccountPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<MedDetailsSearchParam> queryWrapper, @Param("amb") Integer amb,
        @Param("completed") Integer completed, @Param("refunded") Integer refunded,
        @Param("dispenseEnum") Integer dispenseEnum);

//    /**
//     * 门诊发药明细流水账
//     *
//     * @param page 分页
//     * @param queryWrapper 查询条件
//     * @param amb 发药类型：门诊
//     * @param completed 发药状态：已发药
//     * @param refunded 发药状态：已退药
//     * @return 门诊发药明细流水账
//     */
//    Page<MedDetailedAccountPageDto> selectAmbMedicationDispenseDetailPage(
//        @Param("page") Page<MedDetailedAccountPageDto> page,
//        @Param(Constants.WRAPPER) QueryWrapper<MedDetailsSearchParam> queryWrapper, @Param("amb") Integer amb,
//        @Param("completed") Integer completed, @Param("refunded") Integer refunded)

}
