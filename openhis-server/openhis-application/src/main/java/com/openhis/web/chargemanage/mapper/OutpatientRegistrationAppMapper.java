package com.openhis.web.chargemanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.chargemanage.dto.CurrentDayEncounterDto;
import com.openhis.web.chargemanage.dto.PractitionerMetadata;
import com.openhis.web.personalization.dto.ActivityDeviceDto;

/**
 * 门诊挂号 应用Mapper
 */
@Repository
public interface OutpatientRegistrationAppMapper {
    /**
     * 查询医生
     */
    IPage<PractitionerMetadata> getPractitionerMetadataPage(@Param("page") Page<PractitionerMetadata> page,
        @Param("orgId") Long orgId, @Param("RoleCode") String RoleCode,
        @Param(Constants.WRAPPER) QueryWrapper<PractitionerMetadata> queryWrapper);

    /**
     * 根据病人id和科室id查询当日挂号次数
     */
    Integer getNumByPatientIdAndOrganizationId(@Param("patientId") Long patientId,
        @Param("serviceTypeId") Long serviceTypeId, @Param("cancelled") Integer cancelled);

    /**
     * 查询当日就诊数据
     * 
     * @param page 分页参数
     * @param classEnum 就诊类型
     * @param participantType 参与者类型
     * @param queryWrapper 查询条件
     * @param register 收费项目类型:挂号
     * @param paymentStatus 支付状态:成功
     * @return 当日就诊数据
     */
    IPage<CurrentDayEncounterDto> getCurrentDayEncounter(@Param("page") Page<CurrentDayEncounterDto> page,
        @Param("classEnum") Integer classEnum, @Param("participantType") String participantType,
        @Param(Constants.WRAPPER) QueryWrapper<CurrentDayEncounterDto> queryWrapper,
        @Param("register") Integer register, @Param("paymentStatus") Integer paymentStatus);

    /**
     * 查询item绑定的信息(耗材或诊疗)
     * 
     * @param itemId itemId
     * @param devActable 绑定的表名(耗材或诊疗)
     * @return item绑定的信息
     */
    List<ActivityDeviceDto> getTmpActivityList(@Param("itemId") String itemId, @Param("devActable") String devActable);

}
