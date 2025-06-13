/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;

import java.math.BigDecimal;

/**
 * 【3202】文件输出实体
 *
 * @author SunJQ
 * @date 2025-04-22
 */
public class Financial3202FileResult {
    // 1. 人员编号（字符型，30位，必填）
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 就诊ID（字符型，30位，非必填）
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 3. 结算ID（字符型，30位，非必填）
    @JSONField(name = "setl_id")
    private String setlId;

    // 4. 发送方报文ID（字符型，30位，必填）
    @JSONField(name = "msgid")
    private String msgid;

    // 5. 对账结果（字符型，6位，必填）
    @JSONField(name = "stmt_rslt")
    private String stmtRslt;

    // 6. 退费结算标志（字符型，3位，必填）
    @JSONField(name = "refd_setl_flag")
    private String refdSetlFlag;

    // 7. 备注（字符型，500位，非必填）
    @JSONField(name = "memo")
    private String memo;

    // 8. 医疗费总额（数值型，16位含2位小数，非必填）
    @JSONField(name = "medfee_sumamt")
    private BigDecimal medfeeSumamt;

    // 9. 基金支付总额（数值型，16位含2位小数，非必填）
    @JSONField(name = "fund_pay_sumamt")
    private BigDecimal fundPaySumamt;

    // 10. 个人账户支出（数值型，16位含2位小数，非必填）
    @JSONField(name = "acct_pay")
    private BigDecimal acctPay;
}
