package com.openhis.yb.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PaymentSettleDto {

    private Date endTime;

    private Date startTime;

    private Long enterId;

    private String contractNo;
}
