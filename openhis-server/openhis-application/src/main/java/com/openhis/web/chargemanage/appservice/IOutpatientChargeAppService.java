/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;
import com.openhis.web.chargemanage.dto.EncounterPatientPrescriptionDto;

import java.util.List;

/**
 * 门诊收费 service
 *
 * @author zwh
 * @date 2025-03-12
 */
public interface IOutpatientChargeAppService {

    /**
     * 查询就诊患者分页列表
     *
     * @param encounterPatientPageParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param request 请求
     * @return 就诊患者分页列表
     */
    R<?> getEncounterPatientPage(EncounterPatientPageParam encounterPatientPageParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request);

    /**
     * 根据就诊id查询患者处方列表
     *
     * @param encounterId 就诊id
     * @return 患者处方列表
     */
    List<EncounterPatientPrescriptionDto> getEncounterPatientPrescription(Long encounterId);

    /**
     * 医保转自费
     *
     * @param encounterId 就诊id
     * @return 操作结果
     */
    R<?> changeToSelfPay(Long encounterId);

    /**
     * 自费转医保
     *
     * @param encounterId 就诊id
     * @return 操作结果
     */
    R<?> changeToMedicalInsurance(Long encounterId);

    /**
     * 门诊收费页面初始化
     *
     * @return 初始化信息
     */
    R<?> outpatientChargeInit();
}
