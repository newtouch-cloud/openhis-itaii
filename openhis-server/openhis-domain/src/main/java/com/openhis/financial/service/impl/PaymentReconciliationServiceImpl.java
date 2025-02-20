package com.openhis.financial.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.mapper.PaymentReconciliationMapper;
import com.openhis.financial.service.IPaymentReconciliationService;

/**
 * 付款管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PaymentReconciliationServiceImpl extends ServiceImpl<PaymentReconciliationMapper, PaymentReconciliation> implements IPaymentReconciliationService {

}