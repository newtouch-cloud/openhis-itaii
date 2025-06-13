package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.PatientIdentifier;

/**
 * 患者标识管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IPatientIdentifierService extends IService<PatientIdentifier> {

    /**
     * 查询病人标识
     *
     * @param patientId 患者Id
     */
    PatientIdentifier selectByPatientId(Long patientId);

    /**
     * 查询病人标识
     *
     * @param patientId 患者Id
     * @param typeCode 患者标识
     */
    boolean updateTypeByPatientId(Long patientId, String typeCode);

}