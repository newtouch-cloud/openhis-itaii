/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 *  收费项目明细
 *
 * @author yuxj
 * @date 2025-04-22
 */
@Data
@Accessors(chain = true)
public class EleInvoiceChargeDetailDto {

    // 医疗收费项目类别;
    private String ybType;
    // 数量
    private Integer number;
    // 金额
    private BigDecimal amt;
    // 自费金额
    private BigDecimal selfAmt;
    // 收费项目名称
    private String chargeName;
    // 收费项目代码
    private String chargeCode;

}
