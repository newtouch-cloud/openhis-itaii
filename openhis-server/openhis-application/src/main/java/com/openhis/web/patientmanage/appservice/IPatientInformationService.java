package com.openhis.web.patientmanage.appservice;

import com.openhis.web.patientmanage.dto.PatientInfoInitDto;

/**
 * 病人管理——病人信息
 *
 * @author Wuser
 * @date 2025/3/15
 */
public interface IPatientInformationService {

    /**
     * 获取病人信息记录初期数据列表
     *
     * @return 病人信息记录初期数据列表
     */
    PatientInfoInitDto getPatientInfoInit();
}
