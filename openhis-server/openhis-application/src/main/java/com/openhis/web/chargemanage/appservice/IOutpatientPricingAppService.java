/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.PatientInfoDto;

/**
 * 门诊划价 service
 *
 * @author yangmo
 * @date 2025-04-14
 */
public interface IOutpatientPricingAppService {

    /**
     * 查询就诊患者信息
     *
     * @param patientInfoDto 查询条件 (前端可传 statusEnum 区分就诊状态tab)
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 就诊患者信息
     */
    IPage<PatientInfoDto> getPatientInfo(PatientInfoDto patientInfoDto, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request);

    /**
     * 查询医嘱信息
     *
     * @param adviceBaseDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param locationId 药房id
     * @param organizationId 患者挂号对应的科室id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 医嘱信息
     */
    IPage<AdviceBaseDto> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto, String searchKey, Long locationId,
        Long organizationId, Integer pageNo, Integer pageSize);

}
