/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 【2206】医保基金支付详情
 *
 * @author SunJQ
 * @date 2025-04-19
 */
@Data
@Accessors(chain = true)
public class Clinic2206FundPaymentResult {
    // 主键字段（根据Y标识判断）
    @JSONField(name = "fund_pay_type")
    private String fundPayType; // 基金支付类型

    // 数值型字段使用BigDecimal保证精度
    @JSONField(name = "inscp_scp_amt")
    private BigDecimal inscpScpAmt; // 符合政策范围金额

    @JSONField(name = "crt_payb_lmt_amt")
    private BigDecimal crtPaybLmtAmt; // 本次可支付限额金额

    @JSONField(name = "fund_payamt")
    private BigDecimal fundPayamt; // 基金支付金额

    // 字符型字段
    @JSONField(name = "fund_pay_type_name")
    private String fundPayTypeName; // 基金支付类型名称

    @JSONField(name = "setl_proc_info")
    private String setlProcInfo; // 结算过程信息
}
