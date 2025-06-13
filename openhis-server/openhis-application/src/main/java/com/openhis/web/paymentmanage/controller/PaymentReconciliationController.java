/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.openhis.web.paymentmanage.dto.PaymentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.web.chargemanage.dto.OutpatientRegistrationAddParam;
import com.openhis.web.chargemanage.dto.OutpatientRegistrationSettleParam;
import com.openhis.web.paymentmanage.appservice.IChargeBillService;
import com.openhis.web.paymentmanage.appservice.IEleInvoiceService;
import com.openhis.web.paymentmanage.appservice.IPaymentRecService;
import com.openhis.web.paymentmanage.dto.CancelPaymentDto;
import com.openhis.web.paymentmanage.dto.PaymentDto;
import com.openhis.web.paymentmanage.dto.PrePaymentDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    @Autowired
    private IEleInvoiceService eleInvoiceService;

    @Autowired
    private IChargeBillService iChargeBillService;

    /**
     * 预付款
     *
     * @param prePaymentDto 付款实体
     */
    @PostMapping("/precharge")
    public R<?> savePrePayment(@Validated @RequestBody PrePaymentDto prePaymentDto) {
        return paymentReconciliationService.prePayment(prePaymentDto);
    }

    @PostMapping("/unprecharge")
    public R<?> unPrePayment(Long encounterId) {
        return paymentReconciliationService.unPrePayment(encounterId);
    }

    /**
     * 付款
     *
     * @param paymentDto 付款实体
     */
    @PostMapping("/charge")
    public R<?> savePayment(@Validated @RequestBody PaymentDto paymentDto) {
        R<?> result = paymentReconciliationService.savePayment(paymentDto);
        // 付款成功后，开具发票
        if (result.getCode() == 200) {
            PaymentReconciliation paymentRecon = null;
            if (PaymentReconciliation.class.isAssignableFrom(result.getData().getClass())) {
                paymentRecon = (PaymentReconciliation)result.getData();
            }
            R<?> eleResult = eleInvoiceService.invoiceMZMake(paymentRecon.getId(), paymentDto.getEncounterId());
            if (eleResult.getCode() != 200) {
                // 因收费成功前端需要关闭弹窗，此处信息仅用于提示所以返回ok
                return R.ok(null, " 收费成功，电子发票开具失败 :" + eleResult.getMsg());
            }
            Map detail = iChargeBillService.getDetail(paymentRecon.getId());
            return R.ok(detail);
        }
        return result;
    }

    /**
     * 取消付款
     *
     * @param cancelPaymentDto 付款实体
     */
    @PostMapping("/uncharge")
    public R<?> cancelPayment(@RequestBody CancelPaymentDto cancelPaymentDto) {
        R<?> result = paymentReconciliationService.cancelPayment(cancelPaymentDto);
        // 取消付款成功后，开具发票
        if (result.getCode() == 200) {
            PaymentReconciliation paymentRecon = null;
            if (PaymentReconciliation.class.isAssignableFrom(result.getData().getClass())) {
                paymentRecon = (PaymentReconciliation)result.getData();
            }
            R<?> eleResult = eleInvoiceService.invoiceWriteoff(paymentRecon.getRelationId(), cancelPaymentDto.getReason());
            if (eleResult.getCode() != 200) {
                // 因取消付款成功前端需要关闭弹窗，此处信息仅用于提示所以返回ok
                return R.ok(null, " 取消付款成功，电子发票开具失败 :" + eleResult.getMsg());
            }
        }
        return result;
    }

    /**
     * 支付列表
     *
     * @param searchKey
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page")
    public R<?> paymentPage(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return R.ok(paymentReconciliationService.getPage(searchKey, pageNo, pageSize, request));
    }

    /**
     * 支付详情
     *
     * @param paymentDto
     * @return
     */
    @PostMapping("/detail")
    public R<?> paymentDetail(@RequestBody PaymentDto paymentDto) {
        return R.ok(paymentReconciliationService.getDetail(paymentDto));
    }

    /**
     * 挂号收费（挂号收费先医保挂号，收费成功后再本系统挂号）
     *
     * @param outpatientRegistrationAddParam
     * @return
     */
    @PostMapping("/reg-pre-pay")
    public R<?> regPrePay(@Valid @RequestBody OutpatientRegistrationAddParam outpatientRegistrationAddParam) {
        return paymentReconciliationService.regPrePay(outpatientRegistrationAddParam);
    }

    /**
     * 挂号收费（挂号收费先医保挂号，收费成功后再本系统挂号）
     *
     * @param outpatientRegistrationAddParam
     * @return
     */
    @PostMapping("/reg-pay")
    public R<?> regPay(@Valid @RequestBody OutpatientRegistrationSettleParam outpatientRegistrationAddParam) {
        R<?> result = paymentReconciliationService.regPay(outpatientRegistrationAddParam,
            outpatientRegistrationAddParam.getChrgBchno(), outpatientRegistrationAddParam.getPaymentDetails());
        // 付款成功后，开具发票
        if (result.getCode() == 200) {
            PaymentReconciliation paymentRecon = null;
            if (PaymentReconciliation.class.isAssignableFrom(result.getData().getClass())) {
                paymentRecon = (PaymentReconciliation)result.getData();
            }
            Long encounterId = paymentRecon.getEncounterId();
            R<?> eleResult = eleInvoiceService.invoiceRegMake(paymentRecon.getId(), encounterId);
            if (eleResult.getCode() != 200) {
                // 因收费成功前端需要关闭弹窗，此处信息仅用于提示所以返回ok
                return R.ok(null, " 收费成功，电子发票开具失败 :" + eleResult.getMsg());
            }
            Map detail = iChargeBillService.getDetail(paymentRecon.getId());
            return R.ok(detail);
        }
        return result;
    }
}
