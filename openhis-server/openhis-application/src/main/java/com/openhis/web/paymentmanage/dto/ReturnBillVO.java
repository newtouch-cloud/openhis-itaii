package com.openhis.web.paymentmanage.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 日结单返回实体
 */
@Data
public class ReturnBillVO {
    private String paymentNo;//收费单
    private BigDecimal totalAmount;//应收
    private BigDecimal paidAmount;//实收
    private String invoiceNo;//发票编号
}
