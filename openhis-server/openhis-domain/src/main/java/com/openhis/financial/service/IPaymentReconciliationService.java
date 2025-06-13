package com.openhis.financial.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.common.enums.PaymentStatus;
import com.openhis.common.enums.ybenums.YbMdtrtCertType;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.model.PaymentResult;
import com.openhis.financial.model.PaymentedItemModel;
import com.openhis.financial.model.PrePaymentResult;

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

    /**
     * 更新付款状态：退款中
     *
     * @param paymentIdList 支付id列表
     */
    void updateRefundingStatus(List<Long> paymentIdList);

    PrePaymentResult prePayment(YbMdtrtCertType ybMdtrtCertType, String busiCardInfo, String contractBusNo,
        List<PaymentedItemModel> paymentedItemList);

    PaymentResult settle(String payTransNo, YbMdtrtCertType byValue, String busiCardInfo, Integer minpacuntDrugTracCnt,
        Integer mcsTracCnt);

    void updatePaymentStatusById(Long id, PaymentStatus success);

    void updatePaymentStatusAndSettleIdsById(Long id, PaymentStatus success, List<String> settleId);
}
