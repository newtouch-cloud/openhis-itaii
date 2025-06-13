/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;
import com.openhis.web.chargemanage.dto.RefundItemParam;

/**
 * 门诊退费 service
 *
 * @author zwh
 * @date 2025-03-15
 */
public interface IOutpatientRefundAppService {

    /**
     * 根据就诊id查询患者的账单
     *
     * @param encounterId 就诊id
     * @return 患者账单列表
     */
    R<?> getEncounterPatientPayment(Long encounterId);

    /**
     * 根据账单退费
     *
     * @param refundItemList 退费项目id列表
     * @return 操作结果
     */
    R<?> refundPayment(List<RefundItemParam> refundItemList);

    /**
     * 查询结算过的就诊患者分页列表
     *
     * @param encounterPatientPageParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param request 请求
     * @return 就诊患者分页列表
     */
    R<?> getBilledEncounterPatientPage(EncounterPatientPageParam encounterPatientPageParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 门诊退费页面初始化
     *
     * @return 初始化信息
     */
    R<?> outpatientRefundInit();

    /**
     * 根据就诊id查询患者的退费账单
     *
     * @param encounterId 就诊id
     * @return 退费账单列表
     */
    R<?> getEncounterPatientRefund(Long encounterId);

    /**
     * 根据就诊id查询患者因退费重新生成的账单
     *
     * @param encounterId 就诊id
     * @return 重新生成的账单列表
     */
    R<?> getRegenerateCharge(Long encounterId);

    /**
     * 校验是否可以退费（耗材/药品是否已退，诊疗是否取消执行）
     *
     * @param chargeItemIdList 收费项目id列表
     * @return 是否可退
     */
    R<?> verifyRefundable(List<Long> chargeItemIdList);
}
