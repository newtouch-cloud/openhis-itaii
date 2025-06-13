package com.openhis.financial.model;

import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.mapper.PaymentReconciliationMapper;
import com.openhis.yb.dto.Clinic2206FundPaymentResult;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class PrePaymentResultModel {

   private List<PaymentRecDetailDto> details;

   private PaymentReconciliation paymentReconciliation;

   private String paymentId;
}
