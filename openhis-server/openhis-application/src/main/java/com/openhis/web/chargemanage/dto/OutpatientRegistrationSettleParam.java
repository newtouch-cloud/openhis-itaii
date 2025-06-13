package com.openhis.web.chargemanage.dto;

import com.openhis.web.paymentmanage.dto.PaymentDetailDto;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.util.List;

@Data
@Accessors(chain = true)
public class OutpatientRegistrationSettleParam {

    OutpatientRegistrationAddParam outpatientRegistrationAddParam;

    String chrgBchno;//收费批次号 付款时必传

    String busNo;//挂号no

    List<PaymentDetailDto> paymentDetails;//支付渠道 付款时必传

}
