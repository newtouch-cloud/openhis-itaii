package com.openhis.web.paymentmanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.paymentmanage.dto.PaymentSettleDto;
import com.openhis.web.paymentmanage.dto.ReportVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;

public interface IChargeBillService {
    /**
     *
     * @param paymentId
     * @return
     */
    Map getDetail(Long paymentId);

    /**
     *
     * @param
     * @return
     */
    Map getTotal(String startTime, String endTime, Long entererId);
}
