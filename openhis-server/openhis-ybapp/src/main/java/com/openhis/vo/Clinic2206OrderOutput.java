/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 【2206】医保预结算的输出参数
 *
 * @author SunJQ
 * @date 2025-04-19
 */
@Data
@Accessors(chain = true)
public class Clinic2206OrderOutput {
    // 主键字段
    private String mdtrtId; // 就诊ID

    private String psnNo; // 人员编号

    private String psnName; // 人员姓名

    private String psnCertType; // 人员证件类型

    private String certno; // 证件号码

    private String gend; // 性别

    private String naty; // 民族

    private Date brdy; // 出生日期

    private BigDecimal age; // 年龄

    private String insutype; // 险种类型

    private String psnType; // 人员类别

    private String cvlservFlag; // 公务员标志

    private Date setlTime; // 结算时间

    private String mdtrtCertType; // 就诊凭证类型

    private String medType; // 医疗类别

    private BigDecimal medfeeSumamt; // 医疗费总额

    private BigDecimal fulamtOwnpayAmt; // 全自费金额

    private BigDecimal overlmtSelfpay; // 超限价自费费用

    private BigDecimal preselfpayAmt; // 先行自付金额

    private BigDecimal inscpScpAmt; // 符合政策范围金额

    private BigDecimal actPayDedc; // 实际支付起付线

    private BigDecimal hifpPay; // 基本医疗保险统筹基金支出

    private BigDecimal poolPropSelfpay; // 基本医疗保险统筹基金支付比例

    private BigDecimal cvlservPay; // 公务员医疗补助资金支出

    private BigDecimal hifesPay; // 企业补充医疗保险基金支出

    private BigDecimal hifmiPay; // 居民大病保险资金支出

    private BigDecimal hifobPay; // 职工大额医疗费用补助基金支出

    private BigDecimal mafPay; // 医疗救助基金支出

    private BigDecimal othPay; // 其他支出

    private BigDecimal fundPaySumamt; // 基金支付总额

    private BigDecimal psnPartAmt; // 个人负担总金额

    private BigDecimal acctPay; // 个人账户支出

    private BigDecimal psnCashPay; // 个人现金支出

    private BigDecimal hospPartAmt; // 医院负担金额

    private BigDecimal balc; // 余额

    private BigDecimal acctMulaidPay; // 个人账户共济支付金额

    private String medinsSetlId; // 医药机构结算ID  存放发送方报文ID

    private String clrOptins; // 清算经办机构

    private String clrWay; // 清算方式

    private String clrType; // 清算类别

    private BigDecimal hifdmPay; // 伤残人员医疗保障基金支出

    private String expContent; // 字段扩展

    private List<Clinic2206FundPaymentResult> setldetail;// 结算详细信息

    private String mdtrtCertNo; // 就诊凭证编号

    private String chrgBchno; // 收费批次号

    private String busNo; // 挂号采访码
}
