/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.common.core.domain.R;
import com.openhis.common.enums.PaymentStatus;
import com.openhis.web.chargemanage.appservice.IOutpatientRefundAppService;
import com.openhis.web.chargemanage.dto.EncounterPatientPaymentDto;
import com.openhis.web.chargemanage.mapper.OutpatientRefundAppMapper;

/**
 * 门诊退费 impl
 *
 * @author zwh
 * @date 2025-03-15
 */
@Service
public class OutpatientRefundAppServiceImpl implements IOutpatientRefundAppService {

    @Autowired
    private OutpatientRefundAppMapper outpatientRefundAppMapper;

    /**
     * 根据就诊id查询患者的账单
     *
     * @param encounterId 就诊id
     * @return 患者账单列表
     */
    @Override
    public R<?> getEncounterPatientPayment(Long encounterId) {
        List<EncounterPatientPaymentDto> patientPaymentList =
            outpatientRefundAppMapper.selectEncounterPatientPayment(encounterId, PaymentStatus.SUCCESS.getValue(),
                PaymentStatus.REFUND_ALL.getValue(), PaymentStatus.REFUND_PART.getValue());
        return R.ok(patientPaymentList);
    }

    /**
     * 根据账单退费
     *
     * @param paymentIdList 付费id列表
     * @return 操作结果
     */
    @Override
    public R<?> refundPayment(List<Long> paymentIdList) {

        // 医生同意退费
        // 查询所有的chargeid，对应生成一个负的chargeitem
        // 根据每个 chargeitem判断对应的服务，药品，耗材是否已发放执行
        // 若已发放/执行需要对应的发药人/执行人审批（药品耗材需要先退药）
        return null;
    }
}
