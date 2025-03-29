/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.payment.controller;

import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.payment.appservice.IPaymentRecService;
import com.openhis.web.payment.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 付款管理
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@RestController
@RequestMapping("/payment/payment")
@Slf4j
@AllArgsConstructor
public class PaymentReconciliationController {

    @Autowired
    private IPaymentRecService paymentReconciliationService;

    /**
     * 付款
     *
     * @param paymentDto 付款实体
     */
    @PostMapping("/charge")
    public R<?> savePayment(@Validated @RequestBody PaymentDto paymentDto) {
        return paymentReconciliationService.savePayment(paymentDto);
    }

    /**
     * 取消付款
     *
     * @param paymentDto 付款实体
     */
    @PostMapping("/uncharge")
    public R<?> cancelPayment(@RequestBody PaymentDto paymentDto) {
        return paymentReconciliationService.cancelPayment(paymentDto);
    }

    /**
     * 支付列表
     * @param searchKey
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @PostMapping("/page")
    public R<?> paymentPage(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request){
        return R
                .ok(paymentReconciliationService.getPage(searchKey, pageNo, pageSize, request));
    }

    /**
     * 支付详情
     * @param paymentDto
     * @return
     */
    @PostMapping("/detail")
    public R<?> paymentDetail(@RequestBody PaymentDto paymentDto){
        return R.ok(paymentReconciliationService.getDetail(paymentDto));
    }

}
