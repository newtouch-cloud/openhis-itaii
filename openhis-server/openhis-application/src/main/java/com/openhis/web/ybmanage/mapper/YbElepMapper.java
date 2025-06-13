/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.ybmanage.dto.VeriPrescriptionDetailInfoDto;
import com.openhis.web.ybmanage.dto.VeriPrescriptionInfoDto;
import com.openhis.web.ybmanage.dto.VeriPrescriptionParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mapper提供sql服务
 *
 * @author yuxj
 * @date 2025-05-06
 */
@Repository
public interface YbElepMapper {
    /**
     * 医保电子处方查询
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 就诊病人列表
     */
    Page<VeriPrescriptionInfoDto> getVeriPrescriptionInfo(@Param("page") Page<VeriPrescriptionInfoDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<VeriPrescriptionParam> queryWrapper);


    /**
     * 处方详细信息获取
     *
     * @param prescriptionNo 处方号
     * @return 处方详细信息
     */
    List<VeriPrescriptionDetailInfoDto> getVeriPrescriptionDetailInfo(@Param("prescriptionNo") String prescriptionNo);

}
