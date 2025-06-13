/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 预结算结果集
 *
 * @author SunJQ
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
public class Clinic2206OrderResultDto {

    @JSONField(name = "medfee_sumamt")
    private BigDecimal medfeeSumamt; // 医疗费总额

    @JSONField(name = "fulamt_ownpay_amt")
    private BigDecimal fulamtOwnpayAmt; // 全自费金额

    @JSONField(name = "overlmt_selfpay")
    private BigDecimal overlmtSelfpay; // 超限价自费费用

    @JSONField(name = "preselfpay_amt")
    private BigDecimal preselfpayAmt; // 先行自付金额

    @JSONField(name = "inscp_scp_amt")
    private BigDecimal inscpScpAmt; // 符合政策范围金额

    @JSONField(name = "act_pay_dedc")
    private BigDecimal actPayDedc; // 实际支付起付线

    @JSONField(name = "hifp_pay")
    private BigDecimal hifpPay; // 基本医疗保险统筹基金支出

    @JSONField(name = "pool_prop_selfpay")
    private BigDecimal poolPropSelfpay; // 基本医疗保险统筹基金支付比例

    @JSONField(name = "cvlserv_pay")
    private BigDecimal cvlservPay; // 公务员医疗补助资金支出

    @JSONField(name = "hifes_pay")
    private BigDecimal hifesPay; // 企业补充医疗保险基金支出

    @JSONField(name = "hifmi_pay")
    private BigDecimal hifmiPay; // 居民大病保险资金支出

    @JSONField(name = "hifob_pay")
    private BigDecimal hifobPay; // 职工大额医疗费用补助基金支出

    @JSONField(name = "maf_pay")
    private BigDecimal mafPay; // 医疗救助基金支出

    @JSONField(name = "oth_pay")
    private BigDecimal othPay; // 其他支出

    @JSONField(name = "fund_pay_sumamt")
    private BigDecimal fundPaySumamt; // 基金支付总额

    @JSONField(name = "psn_part_amt")
    private BigDecimal psnPartAmt; // 个人负担总金额

    @JSONField(name = "acct_pay")
    private BigDecimal acctPay; // 个人账户支出

    @JSONField(name = "psn_cash_pay")
    private BigDecimal psnCashPay; // 个人现金支出

    @JSONField(name = "hosp_part_amt")
    private BigDecimal hospPartAmt; // 医院负担金额

    @JSONField(name = "hifdm_pay")
    private BigDecimal hifdmPay;

    @JSONField(name = "acct_mulaid_pay")
    private BigDecimal acctMulaidPay; // 个人账户共济支付金额

    private String chrgBchno;//收费批次号

    private Long accountId;//账户id

    private String medType;//医疗类型
}
