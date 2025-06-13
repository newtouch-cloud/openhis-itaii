/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.yb.dto.Clinic2206FundPaymentResult;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 【2208】门诊结算撤销
 *
 * @author SunJQ
 * @date 2025-04-21
 */
@Data
@Accessors(chain = true)
public class Clinic2208UnSetlInfoOutput {

    // 1. 就诊ID（字符型，30位，必填）
    private String mdtrtId;

    // 2. 结算ID（字符型，30位，必填）
    private String setlId;

    // 3. 清算经办机构（字符型，6位）
    private String clrOptins;

    // 4. 结算时间（日期时间型，格式：yyyy-MM-dd HH:mm:ss，必填）
    private Date setlTime;

    // 5. 医疗费总额（数值型，16位含2位小数，必填）
    private BigDecimal medfeeSumamt;

    // 6. 全自费金额（数值型，16位含2位小数，必填）
    private BigDecimal fulamtOwnpayAmt;

    // 7. 超限价自费费用（数值型，16位含2位小数，必填）
    private BigDecimal overlmtSelfpay;

    // 8. 先行自付金额（数值型，16位含2位小数，必填）
    private BigDecimal preselfpayAmt;

    // 9. 符合政策范围金额（数值型，16位含2位小数，必填）
    private BigDecimal inscpScpAmt;

    // 10. 实际支付起付线（数值型，16位含2位小数）
    private BigDecimal actPayDedc;

    // 11. 基本医疗保险统筹基金支出（数值型，16位含2位小数，必填）
    private BigDecimal hifpPay;

    // 12. 统筹基金支付比例（数值型，5位含4位小数，必填）
    private BigDecimal poolPropSelfpay;

    // 13. 公务员医疗补助支出（数值型，16位含2位小数，必填）
    private BigDecimal cvlservPay;

    // 14. 企业补充医保支出（数值型，16位含2位小数，必填）
    private BigDecimal hifesPay;

    // 15. 居民大病保险支出（数值型，16位含2位小数，必填）
    private BigDecimal hifmiPay;

    // 16. 职工大额医疗补助支出（数值型，16位含2位小数，必填）
    private BigDecimal hifobPay;

    // 17. 医疗救助基金支出（数值型，16位含2位小数，必填）
    private BigDecimal mafPay;

    // 18. 其他支出（数值型，16位含2位小数，必填）
    private BigDecimal othPay;

    // 19. 基金支付总额（数值型，16位含2位小数，必填）
    private BigDecimal fundPaySumamt;

    // 20. 个人负担总金额（数值型，16位含2位小数，必填）
    private BigDecimal psnPartAmt;

    // 21. 个人账户支出（数值型，16位含2位小数，必填）
    private BigDecimal acctPay;

    // 22. 余额（数值型，16位含2位小数，必填）
    private BigDecimal balc;

    // 23. 个人账户共济支付（数值型，16位含2位小数，必填）
    private BigDecimal acctMulaidPay;

    // 24. 医院负担金额（数值型，16位含2位小数，必填）
    private BigDecimal hospPartAmt;

    // 25. 医药机构结算ID（字符型，30位，必填）
    private String medinsSetlId;

    // 26. 个人现金支出（数值型，16位含2位小数，必填）
    private BigDecimal pdnCashPay;

    // 27. 伤残人员医疗补助支出（数值型，16位含2位小数，必填）
    private BigDecimal hifdmPay;

    //结算详细信息
    private List<Clinic2206FundPaymentResult> setldetail;//结算详细信息
}
