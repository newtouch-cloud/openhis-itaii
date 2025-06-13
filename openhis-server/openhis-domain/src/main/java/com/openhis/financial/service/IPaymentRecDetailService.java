package com.openhis.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.common.enums.PaymentResult;
import com.openhis.financial.domain.PaymentRecDetail;

/**
 * 付款管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IPaymentRecDetailService extends IService<PaymentRecDetail> {

    /**
     * 根据paymentId更改结算结果
     * @param id
     * @param paid
     */
    void updateResultByPaymentId(Long id, PaymentResult paid);
}