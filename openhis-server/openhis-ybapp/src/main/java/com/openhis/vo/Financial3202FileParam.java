/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 【3202】文件输入实体
 *
 * @author SunJQ
 * @date 2025-04-22
 */
@Data
@Accessors(chain = true)
public class Financial3202FileParam {
    // 1. 结算ID（字符型，30位，必填）
    @JSONField(name = "setl_id")
    private String setlId;

    // 2. 就诊ID（字符型，30位，必填）
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 3. 人员编号（字符型，30位，必填）
    @JSONField(name = "psn_no")
    private String psnNo;

    // 4. 医疗费总额（数值型，16位含2位小数，必填）
    @JSONField(name = "medfee_sumamt")
    private BigDecimal medfeeSumamt;

    // 5. 基金支付总额（数值型，16位含2位小数，必填）
    @JSONField(name = "fund_pay_sumamt")
    private BigDecimal fundPaySumamt;

    // 6. 个人账户支出（数值型，16位含2位小数，必填）
    @JSONField(name = "acct_pay")
    private BigDecimal acctPay;

    // 7. 退费结算标志（字符型，3位，必填）
    @JSONField(name = "refd_setl_flag")
    private String refdSetlFlag;
}
