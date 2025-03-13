package com.openhis.web.chargemanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.chargemanage.dto.CurrentDayEncounterDto;
import com.openhis.web.chargemanage.dto.PractitionerMetadata;

/**
 * 门诊挂号 应用Mapper
 */
@Repository
public interface OutpatientRegistrationAppMapper {
    /**
     * 查询医生
     */
    IPage<PractitionerMetadata> getPractitionerMetadataPage(@Param("page") Page<PractitionerMetadata> page,
        @Param("locationId") Long locationId, @Param("RoleCode") String RoleCode,
        @Param(Constants.WRAPPER) QueryWrapper<PractitionerMetadata> queryWrapper);

    /**
     * 根据病人id和科室id查询当日挂号次数
     */
    Integer getNumByPatientIdAndOrganizationId(@Param("patientId") Long patientId,
        @Param("serviceTypeId") Long serviceTypeId);

    /**
     * 查询当日就诊数据
     * 
     * @param page 分页参数
     * @param participantType 参与者类型
     * @param queryWrapper 查询条件
     * @return 当日就诊数据
     */
    IPage<CurrentDayEncounterDto> getCurrentDayEncounter(@Param("page") Page<CurrentDayEncounterDto> page,
        @Param("participantType") String participantType,
        @Param(Constants.WRAPPER) QueryWrapper<CurrentDayEncounterDto> queryWrapper);

}
