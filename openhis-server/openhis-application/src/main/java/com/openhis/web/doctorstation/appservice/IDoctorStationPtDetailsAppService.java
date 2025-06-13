package com.openhis.web.doctorstation.appservice;

import com.core.common.core.domain.R;

/**
 * 医生站-患者详情应用类
 *
 * @author liuhr
 * @date 2025/4/10
 */
public interface IDoctorStationPtDetailsAppService {

    /**
     * 查询患者详情
     *
     * @param encounterId 就诊Id
     * @return 患者详情
     */
    R<?> getPtDetails(Long encounterId);

}
