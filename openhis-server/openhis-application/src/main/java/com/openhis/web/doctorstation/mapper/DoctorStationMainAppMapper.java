package com.openhis.web.doctorstation.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.doctorstation.dto.PatientInfoDto;

/**
 * 医生站-主页面 应用Mapper
 */
@Repository
public interface DoctorStationMainAppMapper {

    /**
     * 查询就诊患者信息
     * 
     * @param page 分页参数
     * @param participantType 参与者类型
     * @param ClinicalStatus 过敏史状态
     * @param userId 当前登录账号ID
     * @param currentUserOrganizationId 当前登录账号所属的科室ID
     * @param queryWrapper 查询条件
     * @return 就诊患者信息
     */
    IPage<PatientInfoDto> getPatientInfo(@Param("page") Page<PatientInfoDto> page,
        @Param("participantType") String participantType, @Param("ClinicalStatus") Integer ClinicalStatus,
        @Param("userId") Long userId, @Param("currentUserOrganizationId") Long currentUserOrganizationId,
        @Param(Constants.WRAPPER) QueryWrapper<PatientInfoDto> queryWrapper);

}
