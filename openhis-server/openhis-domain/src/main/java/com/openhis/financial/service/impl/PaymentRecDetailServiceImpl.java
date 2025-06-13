package com.openhis.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.openhis.common.enums.PaymentResult;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.mapper.PaymentRecDetailMapper;
import com.openhis.financial.service.IPaymentRecDetailService;

/**
 * 付款管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PaymentRecDetailServiceImpl extends ServiceImpl<PaymentRecDetailMapper, PaymentRecDetail> implements IPaymentRecDetailService {
    @Override
    public void updateResultByPaymentId(Long id, PaymentResult paid) {
        baseMapper.update(new PaymentRecDetail() .setResultEnum(PaymentResult.PAID.getValue()),new LambdaQueryWrapper<PaymentRecDetail>().eq(PaymentRecDetail::getReconciliationId,id));
    }
}