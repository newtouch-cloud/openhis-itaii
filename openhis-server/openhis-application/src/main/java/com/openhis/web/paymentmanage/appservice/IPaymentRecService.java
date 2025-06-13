/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.chargemanage.dto.OutpatientRegistrationAddParam;
import com.openhis.web.chargemanage.dto.OutpatientRegistrationSettleParam;
import com.openhis.web.paymentmanage.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
     * 
     * @param paymentDto 入参
     * @return 结果
     */
    R<?> savePayment(PaymentDto paymentDto);

    /**
     * 取消付款
     * 
     * @param cancelPaymentDto 入参
     * @return 结果
     */
    R<?> cancelPayment(CancelPaymentDto cancelPaymentDto);

    /**
     * 取消付款
     *
     * @param cancelPaymentDto 入参
     * @return 结果
     */
    R<?> cancelRegPayment(CancelPaymentDto cancelPaymentDto);

    /**
     * 支付列表
     * 
     * @param searchKey 查询条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 结果
     */
    IPage<PaymentVO> getPage(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 获取支付详情
     * 
     * @param paymentDto 入参
     * @return 结果
     */
    List<PaymentDetailDto> getDetail(PaymentDto paymentDto);

    /**
     * 预结算
     * @param prePaymentDto 预结算入参
     * @return 预结算结果
     */
    R<?> prePayment(PrePaymentDto prePaymentDto);

    /**
     * 挂号预结算
     * @param outpatientRegistrationAddParam 挂号参数
     * @return 预结算结果
     */
    R<?> regPrePay(OutpatientRegistrationAddParam outpatientRegistrationAddParam);

    /**
     * 挂号结算
     * @param outpatientRegistrationSettleParam 挂号参数
     * @return 预结算结果
     */
    R<?> regPay(OutpatientRegistrationSettleParam outpatientRegistrationSettleParam, String chrgBchno, List<PaymentDetailDto> paymentDetails);

    /**
     * 取消预结算
     * @param encounterId 就诊id
     * @return
     */
    R<?> unPrePayment(Long encounterId);
}
