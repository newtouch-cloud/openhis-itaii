package com.openhis.web.paymentmanage.controller;

import com.core.common.core.domain.R;
import com.core.common.exception.ServiceException;
import com.openhis.administration.domain.Invoice;
import com.openhis.common.enums.ybenums.YbEncounterClass;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.web.paymentmanage.appservice.IChargeBillService;
import com.openhis.web.paymentmanage.appservice.IEleInvoiceService;
import com.openhis.web.paymentmanage.dto.MakeInvoiceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 电子发票
 *
 * @author yuxj
 * @date 2025-05-28
 */
@RestController
@RequestMapping("/invoice")
@Slf4j
public class EleInvoiceController {

    @Autowired
    private IEleInvoiceService eleInvoiceService;

    @Autowired
    private IChargeBillService iChargeBillService;

    /**
     * 电子票据补开接口
     *
     * @param makeInvoiceDto 发票开具dto
     * @return
     */
    @PostMapping("/invoiceReissue")
    public R<?> invoiceReissue(@RequestBody MakeInvoiceDto makeInvoiceDto) {
        // 付款成功后，开具发票
        R<?> result = eleInvoiceService.invoiceReissue(makeInvoiceDto.getPaymentId(), makeInvoiceDto.getEncounterId());
        if (result.getCode() == 200) {
            if (result.getData() == YbEncounterClass.REG.getValue()) {
                // 付款成功后，开具发票
                R<?> eleResult = eleInvoiceService.invoiceRegMake(makeInvoiceDto.getPaymentId(), makeInvoiceDto.getEncounterId());
                if (eleResult.getCode() != 200) {
                    return R.ok(" 挂号电子发票开具失败 :" + eleResult.getMsg());
                }
            } else if (result.getData() == YbEncounterClass.AMB.getValue()) {
                // 付款成功后，开具发票
                R<?> eleResult = eleInvoiceService.invoiceMZMake(makeInvoiceDto.getPaymentId(), makeInvoiceDto.getEncounterId());
                if (eleResult.getCode() != 200) {
                    return R.ok(" 门诊电子发票开具失败 :" + eleResult.getMsg());
                }
            } else if (result.getData() == YbEncounterClass.IMP.getValue()) {
                // 付款成功后，开具发票
                R<?> eleResult = eleInvoiceService.invoiceZYMake(makeInvoiceDto.getPaymentId(), makeInvoiceDto.getEncounterId());
                if (eleResult.getCode() != 200) {
                    return R.ok(" 住院电子发票开具失败 :" + eleResult.getMsg());
                }
            } else {
                return R.ok("电子发票类型不明确！");
            }
        }
        Map detail = iChargeBillService.getDetail(makeInvoiceDto.getPaymentId());
        return R.ok(detail);
    }

    /**
     * 医疗电子票据红冲接口
     *
     * @param paymentId 原付款id
     * @param reason 取消理由
     * @return
     */
    @PostMapping("/invoiceWriteoff")
    public R<?> invoiceWriteoff(Long paymentId, String reason) {
        // 退款成功后，开具发票
        R<?> eleResult = eleInvoiceService.invoiceWriteoff(paymentId, reason);
        if (eleResult.getCode() != 200) {
            return R.ok(" 电子票据红冲失败 :" + eleResult.getMsg());
        }
        Map detail = iChargeBillService.getDetail(paymentId);
        return R.ok(detail);
    }

    /**
     * 医疗电子票据红冲接口
     *
     * @param invoiceId 原付款id
     * @return
     */
    @PutMapping("/invoiceOpen")
    public R<?> invoiceOpen(@RequestParam("invoiceId") String invoiceId) {
        // 退款成功后，开具发票
        Invoice invoice = eleInvoiceService.getInvoiceById(Long.parseLong(invoiceId));
        if(invoice ==null){
            throw new ServiceException("未查询到发票信息");
        }
        return R.ok(invoice.getPictureUrl());
    }

}
