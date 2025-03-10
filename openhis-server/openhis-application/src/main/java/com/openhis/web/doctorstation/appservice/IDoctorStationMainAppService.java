package com.openhis.web.doctorstation.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.openhis.web.doctorstation.dto.PatientInfoDto;

/**
 * 医生站-电子病历 应用Service
 */
public interface IDoctorStationMainAppService {

    /**
     * 查询就诊患者信息
     *
     * @param patientInfoDto 查询条件 (前端传 statusEnum 区分就诊状态tab)
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 就诊患者信息
     */
    IPage<PatientInfoDto> getPatientInfo(PatientInfoDto patientInfoDto, String searchKey, Integer pageNo,
        Integer pageSize);

}
