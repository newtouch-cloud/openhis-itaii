/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3205A】清算申请状态查询（吉林省）
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Clearing3205AResult {
    // 定点医药机构名称
    @JSONField(name = "fixmedins_name")
    private String fixmedinsName;

    // 定点医药机构编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    // 清算状态
    @JSONField(name = "clr_stas")
    private String clrStas;

    // 经办时间
    @JSONField(name = "opt_time")
    private Date optTime;

    // 医疗费总额
    @JSONField(name = "medfee_Sumamt")
    private BigDecimal medfeeSumamt;

    // 医保费用总额
    @JSONField(name = "hi_agre_sumfee")
    private BigDecimal hiAgreSumfee;

    // 基金申报总额
    @JSONField(name = "fund_appy_sum")
    private BigDecimal fundAppySum;

    // 现金支付金额
    @JSONField(name = "cash_payamt")
    private BigDecimal cashPayamt;

    // 个人账户支出
    @JSONField(name = "acct_pay")
    private BigDecimal acctPay;

    // 开始日期
    @JSONField(name = "begndate")
    private Date begndate;

    // 结束日期
    @JSONField(name = "enddate")
    private Date enddate;

    // 清算年月
    @JSONField(name = "clr_ym")
    private String clrYm;

    // 清算经办机构
    @JSONField(name = "clr_optins")
    private String clrOptins;

    // 经办人
    @JSONField(name = "opter_id")
    private String opterId;

    // 经办人姓名
    @JSONField(name = "opter_name")
    private String opterName;

    // 经办机构
    @JSONField(name = "optins_no")
    private String optinsNo;

    // 机构清算申请事件ID
    @JSONField(name = "clr_appy_evt_id")
    private String clrAppyEvtId;
}
