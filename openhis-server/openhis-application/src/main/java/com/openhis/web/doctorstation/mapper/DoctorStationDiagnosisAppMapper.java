package com.openhis.web.doctorstation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.doctorstation.dto.ConditionDefinitionMetadata;
import com.openhis.web.doctorstation.dto.DiagnosisBelongBindingDto;

/**
 * 医生站-诊断 应用Mapper
 */
@Repository
public interface DoctorStationDiagnosisAppMapper {

    /**
     * 查询诊断归属绑定列表
     * 
     * @param page 分页参数
     * @param bindingType1 个人类型
     * @param bindingType2 科室类型
     * @param queryWrapper 查询条件
     * @return 诊断归属绑定列表
     */
    IPage<DiagnosisBelongBindingDto> getDiagnosisBelongBindingPage(@Param("page") Page<DiagnosisBelongBindingDto> page,
        @Param("bindingType1") Integer bindingType1, @Param("bindingType2") Integer bindingType2,
        @Param(Constants.WRAPPER) QueryWrapper<DiagnosisBelongBindingDto> queryWrapper);

    /**
     * 查询病人历史诊断
     * 
     * @param statusEnum 状态
     * @param patientId 患者id
     * @return 病人历史诊断
     */
    List<ConditionDefinitionMetadata> getPatientHistoryList(@Param("statusEnum") Integer statusEnum,
        @Param("patientId") Long patientId);

}
