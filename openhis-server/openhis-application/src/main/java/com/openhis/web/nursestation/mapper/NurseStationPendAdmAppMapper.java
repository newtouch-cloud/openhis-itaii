package com.openhis.web.nursestation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.openhis.web.nursestation.dto.PendingAdmissionDto;

/**
 * 待入科 应用Mapper
 *
 * @author liuhr
 * @date 2025/4/14
 */
@Repository
public interface NurseStationPendAdmAppMapper {

    /**
     * 住院待入科病人信息查询
     *
     * @param classEnum 类别编码(1:住院类型)
     * @param status location的状态可用
     * @param wardForm 病区:4
     * @param bedForm 病床:8
     * @return
     */
    List<PendingAdmissionDto> getPendAdmInfo(@Param("classEnum") Integer classEnum, @Param("status") Integer status,
        @Param("wardForm") Integer wardForm, @Param("bedForm") Integer bedForm);

    /**
     * 病区床位信息查询
     *
     * @param encounterId 就诊Id
     * @return
     */
    List<String> getDescriptionList(@Param("encounterId") Long encounterId);

}
