/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;

import javax.servlet.http.HttpServletRequest;

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
}
