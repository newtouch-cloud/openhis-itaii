package com.openhis.web.paymentmanage.controller;

import com.core.common.core.domain.R;
import com.openhis.web.paymentmanage.appservice.IChargeBillService;
import com.openhis.web.paymentmanage.dto.PaymentSettleDto;
import com.openhis.web.paymentmanage.dto.ReportVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 报表
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@RestController
@RequestMapping("/payment/bill")
@Slf4j
public class ChargeBillController {

    @Autowired
    private IChargeBillService iChargeBillService;

    /**
     * 单次结算账单
     *
     * @param paymentId 付款实体
     */
    @PostMapping("/getDetail")
    public R<?> getDetail(Long paymentId) {
        return R.ok(iChargeBillService.getDetail(paymentId));
    }


    /**
     * 日结算账单
     *
     * @param startTime 结算参数
     * @param endTime 结算参数
     * @param entererId 结算参数
     */
    @GetMapping("/getTotal")
    public R<?> getTotal(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("entererId")Long entererId) {
        return R.ok(iChargeBillService.getTotal(startTime,endTime,entererId));
    }
}
