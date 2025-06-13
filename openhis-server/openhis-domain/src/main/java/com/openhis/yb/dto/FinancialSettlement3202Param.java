/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3202】医药机构费用结算对明细账
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FinancialSettlement3202Param {
    // 1. 结算经办机构（字符型，6位，必填）
    @JSONField(name = "setl_optins")
    private String setlOptins;

    // 2. 文件查询号（字符型，30位，必填）
    @JSONField(name = "file_qury_no")
    private String fileQuryNo;

    // 3. 对账开始日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "stmt_begndate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date stmtBegndate;

    // 4. 对账结束日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "stmt_enddate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date stmtEnddate;

    // 5. 医疗费总额（数值型，16位含2位小数，必填）
    @JSONField(name = "medfee_sumamt")
    private BigDecimal medfeeSumamt;

    // 6. 基金支付总额（数值型，16位含2位小数，必填）
    @JSONField(name = "fund_pay_sumamt")
    private BigDecimal fundPaySumamt;

    // 7. 现金支付金额（数值型，16位含2位小数，必填）
    @JSONField(name = "cash_payamt")
    private BigDecimal cashPayamt;

    // 8. 定点医药机构结算笔数（数值型，10位，必填）
    @JSONField(name = "fixmedins_setl_cnt")
    private Integer fixmedinsSetlCnt;

    // 9. 清算类别（字符型，6位，必填）
    @JSONField(name = "clr_type")
    private String clrType;

    // 10. 退费结算标志（字符型，3位，必填）
    @JSONField(name = "refd_setl_flag")
    private String refdSetlFlag;
}
