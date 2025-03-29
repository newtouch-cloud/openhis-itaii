/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.payment.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import com.openhis.web.payment.dto.PaymentDetailDto;
import com.openhis.web.payment.dto.PaymentDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 付款应用层Service
 *
 * @author SunJQ
 * @date 2025-03-29
 */
public interface IPaymentRecService {
    /**
     * 付款
     * @param paymentDto
     * @return
     */
    R<?> savePayment(PaymentDto paymentDto);
    /**
     * 取消付款
     * @param paymentDto
     * @return
     */
    R<?> cancelPayment(PaymentDto paymentDto);

    /**
     * 支付列表
     * @param searchKey
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    IPage<PaymentDto> getPage(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request);

    List<PaymentDetailDto> getDetail(PaymentDto paymentDto);

}
