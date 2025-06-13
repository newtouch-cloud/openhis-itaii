/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 【3203】清算申请
 *
 * @author SunJQ
 * @date 2025-04-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Financial3203Param {
    private static final long serialVersionUID = 1L;

    // 1. 清算类别（字符型，30位，必填）
    @JSONField(name = "clr_type")
    private String clrType;

    // 10. 清算方式（字符型，30位，必填）
    @JSONField(name = "clr_way")
    private String clrWay;

    // 2. 医疗费总额（数值型，16位含2位小数，必填）
    @JSONField(name = "medfee_sumamt")
    private BigDecimal medfeeSumamt;

    // 3. 医保认可费用总额（数值型，16位含2位小数，必填）
    @JSONField(name = "med_sumfee")
    private BigDecimal medSumfee;

    // 4. 基金申报总额（数值型，16位含2位小数，必填）
    @JSONField(name = "fund_appy_sum")
    private BigDecimal fundAppySum;

    // 5. 现金支付金额（数值型，16位含2位小数，必填）
    @JSONField(name = "cash_payamt")
    private BigDecimal cashPayamt;

    // 6. 个人账户支出（数值型，16位含2位小数，必填）
    @JSONField(name = "acct_pay")
    private BigDecimal acctPay;

    // 7. 开始日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "begndate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begndate;

    // 8. 结束日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "enddate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enddate;

    // 9. 清算年月
    @JSONField(name = "setlym")
    private String setlym;

    // 11. 清算人次
    @JSONField(name = "psntime")
    private Long psntime;
}
