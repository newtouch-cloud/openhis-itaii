package com.openhis.financial.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.common.constant.CommonConstants;
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
public class PaymentReconciliationServiceImpl extends ServiceImpl<PaymentReconciliationMapper, PaymentReconciliation>
    implements IPaymentReconciliationService {

    @Autowired
    private PaymentReconciliationMapper paymentReconciliationMapper;

    /**
     * 根据支付id获取对应收费项目的id列表
     *
     * @param paymentId 支付id
     * @return 收费项目的id列表
     */
    @Override
    public List<Long> getChargeItemIdListByPayment(Long paymentId) {

        // 根据支付id获取支付信息
        PaymentReconciliation paymentReconciliation =
            paymentReconciliationMapper.selectOne(new LambdaQueryWrapper<PaymentReconciliation>()
                .select(PaymentReconciliation::getChargeItemIds).eq(PaymentReconciliation::getId, paymentId));
        if (paymentReconciliation == null) {
            return null;
        }
        // 将收费项目集合转换成列表
        return Arrays.stream(paymentReconciliation.getChargeItemIds().split(CommonConstants.Common.COMMA))
            .map(Long::parseLong).collect(Collectors.toList());
    }
}