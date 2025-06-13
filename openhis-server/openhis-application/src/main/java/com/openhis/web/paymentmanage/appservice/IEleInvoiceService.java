/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.administration.domain.Invoice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 电子发票接口Service
 *
 * @author yuxj
 * @date 2025-04-22
 */
public interface IEleInvoiceService {

    /**
     * 电子票据补开接口
     *
     * @param paymentId 支付ID
     * @param encounterId 就诊ID
     * @return
     */
    R<?> invoiceReissue(Long paymentId, Long encounterId);

    /**
     * 医疗挂号电子票据开具接口
     *
     * @param paymentId 支付ID
     * @param encounterId 就诊ID
     * @return
     */
    R<?> invoiceRegMake(Long paymentId, Long encounterId);

    /**
     * 医疗门诊电子票据开具接口
     *
     * @param paymentId 支付ID
     * @param encounterId 就诊ID
     * @return
     */
    R<?> invoiceMZMake(Long paymentId, Long encounterId);

    /**
     * 医疗住院电子票据开具接口
     *
     * @param paymentId 支付ID
     * @param encounterId 就诊ID
     * @return
     */
    R<?> invoiceZYMake(Long paymentId, Long encounterId);

    /**
     * 医疗电子票据红冲接口
     * 
     * @param paymentId 原付款id
     * @param reason 取消理由
     * @return
     */
    R<?> invoiceWriteoff(Long paymentId, String reason);

    /**
     * 查询已开发票
     * @param invoiceId 主键id
     * @return
     */
    Invoice getInvoiceById(Long invoiceId);
}
