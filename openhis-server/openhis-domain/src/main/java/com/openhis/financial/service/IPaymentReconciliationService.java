package com.openhis.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.financial.domain.PaymentReconciliation;

import java.util.List;

/**
 * 付款管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IPaymentReconciliationService extends IService<PaymentReconciliation> {

    /**
     * 根据支付id获取对应收费项目的id列表
     *
     * @param paymentIdList 支付id列表
     * @return 收费项目的id列表
     */
    List<Long> getChargeItemIdListByPayment(List<Long> paymentIdList);
}