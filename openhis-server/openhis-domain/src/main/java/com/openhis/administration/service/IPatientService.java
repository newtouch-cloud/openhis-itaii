package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.domain.PatientIdentifier;

/**
 * 患者管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IPatientService extends IService<Patient> {

    /**
     * 更新或者插入患者管理
     *
     * @param patient 患者实体
     */
    boolean saveOrUpdatePatient(Patient patient);

    /**
     * 更新或者插入患者管理
     *
     * @param patient 患者实体
     */
    boolean savePatient(Patient patient);

    /**
     * 添加病人
     *
     * @param patient 患者实体
     */
    boolean addPatient(Patient patient);

    /**
     * 更新病人
     *
     * @param patient 患者实体
     */
    boolean updatePatient(Patient patient);


}