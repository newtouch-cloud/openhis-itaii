package com.openhis.financial.service.impl;

import java.util.ArrayList;
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
     * @param paymentIdList 支付id列表
     * @return 收费项目的id列表
     */
    @Override
    public List<Long> getChargeItemIdListByPayment(List<Long> paymentIdList) {

        // 根据支付id获取支付信息
        List<PaymentReconciliation> paymentReconciliationList =
            paymentReconciliationMapper.selectList(new LambdaQueryWrapper<PaymentReconciliation>()
                .select(PaymentReconciliation::getChargeItemIds).in(PaymentReconciliation::getId, paymentIdList));
        if (paymentReconciliationList.isEmpty()) {
            return null;
        }
        // 拆解所有的chargeItemId，拼装成一个集合
        List<String> chargeItemIdList = paymentReconciliationList.stream().map(PaymentReconciliation::getChargeItemIds)
            .collect(Collectors.toList());
        List<Long> chargeItemIds = new ArrayList<>();
        for (String chargeItemId : chargeItemIdList) {
            chargeItemIds.addAll(Arrays.stream(chargeItemId.split(CommonConstants.Common.COMMA)).map(Long::parseLong)
                .collect(Collectors.toList()));
        }
        // 将收费项目集合转换成列表
        return chargeItemIds;
    }
}