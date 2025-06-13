package com.openhis.web.doctorstation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.openhis.web.doctorstation.dto.PatientDetailsDto;

/**
 * 医生站-患者详情
 *
 * @author liuhr
 * @date 2025/4/10
 */
@Repository
public interface DoctorStationPtDetailsAppMapper {

    /**
     * 查询患者详情
     *
     * @param encounterId 就诊Id
     * @param contextMed 收费项目药品类型
     * @param contextDev 收费项目设备类型
     * @param contextAct 收费项目耗材类型
     * @param clinicalStatus 过敏反应阳性
     * @param typeCode 就诊参与者身份类型(1:接诊医生)
     * @param statusList 收费状态List(1:待收费,2:待结算,5:已结算)
     * @return 患者详情
     */
    PatientDetailsDto getPtDetailsList(@Param("encounterId") Long encounterId,
        @Param("contextMed") Integer contextMed, @Param("contextDev") Integer contextDev,
        @Param("contextAct") Integer contextAct, @Param("clinicalStatus") Integer clinicalStatus,
        @Param("form") Integer form,@Param("typeCode") String typeCode, @Param("statusList") List<Integer> statusList);

}
