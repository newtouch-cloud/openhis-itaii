/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 【2207】医保预结算的输出参数
 *
 * @author SunJQ
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
public class Clinic2207OrderResult {
    // 主键字段
    @JSONField(name = "mdtrt_id")
    private String mdtrtId; // 就诊ID

    @JSONField(name = "setl_id")
    private String setlId;// 结算id 2207时有值 2206无值

    @JSONField(name = "psn_no")
    private String psnNo; // 人员编号

    @JSONField(name = "psn_name")
    private String psnName; // 人员姓名

    @JSONField(name = "psn_cert_type")
    private String psnCertType; // 人员证件类型

    @JSONField(name = "certno")
    private String certno; // 证件号码

    @JSONField(name = "gend")
    private String gend; // 性别

    @JSONField(name = "naty")
    private String naty; // 民族

    @JSONField(name = "brdy")
    private Date brdy; // 出生日期

    @JSONField(name = "age")
    private BigDecimal age; // 年龄

    @JSONField(name = "insutype")
    private String insutype; // 险种类型

    @JSONField(name = "psn_type")
    private String psnType; // 人员类别

    @JSONField(name = "cvlserv_flag")
    private String cvlservFlag; // 公务员标志

    @JSONField(name = "setl_time")
    private Date setlTime; // 结算时间

    @JSONField(name = "mdtrt_cert_type")
    private String mdtrtCertType; // 就诊凭证类型

    @JSONField(name = "med_type")
    private String medType; // 医疗类别

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

    @JSONField(name = "balc")
    private BigDecimal balc; // 余额

    @JSONField(name = "acct_mulaid_pay")
    private BigDecimal acctMulaidPay; // 个人账户共济支付金额

    @JSONField(name = "medins_setl_id")
    private String medinsSetlId; // 医药机构结算ID  存放发送方报文ID

    @JSONField(name = "clr_optins")
    private String clrOptins; // 清算经办机构

    @JSONField(name = "clr_way")
    private String clrWay; // 清算方式

    @JSONField(name = "clr_type")
    private String clrType; // 清算类别

    @JSONField(name = "hifdm_pay")
    private BigDecimal hifdmPay; // 伤残人员医疗保障基金支出

    @JSONField(name = "exp_content")
    private String expContent; // 字段扩展

    @JSONField(name = "setldetail")
    private List<Clinic2206FundPaymentResult> setldetail;// 结算详细信息

    @JSONField(serialize = false)
    private String chrgBchno; // 收费批次号
}
