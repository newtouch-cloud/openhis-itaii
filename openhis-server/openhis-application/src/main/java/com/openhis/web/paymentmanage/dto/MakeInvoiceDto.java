package com.openhis.web.paymentmanage.dto;

import lombok.Data;

@Data
public class MakeInvoiceDto {
    private Long paymentId;
    private Long encounterId;
}
