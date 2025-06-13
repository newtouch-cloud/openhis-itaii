package com.openhis.web.patientmanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.administration.domain.Patient;
import com.openhis.web.patientmanage.dto.OutpatientRecordDto;
import com.openhis.web.patientmanage.dto.OutpatientRecordSearchParam;
import com.openhis.web.patientmanage.dto.PatientInformationDto;

/**
 * 病人信息管理
 *
 * @author liuhr
 * @date 2025/2/25
 */
@Repository
public interface PatientManageMapper extends BaseMapper<Patient> {

    /**
     * 病人信息分页查询
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 病人信息列表
     */
    IPage<PatientInformationDto> getPatientPage(@Param("page") Page<PatientInformationDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<PatientInformationDto> queryWrapper);

    /**
     * 门诊信息分页查询
     *
     * @param typeCode 参与者身份类型（枚举类ParticipantType）
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 门诊信息列表
     */
    IPage<OutpatientRecordDto> getOutpatientRecord(@Param("typeCode") String typeCode,
        @Param("page") Page<OutpatientRecordDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<OutpatientRecordDto> queryWrapper);

    /**
     * 获取医生名字列表
     *
     * @return 医生名字列表
     */
    List<String> getDoctorNames();

}
