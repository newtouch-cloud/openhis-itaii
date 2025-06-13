package com.openhis.web.ybmanage.vo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Settlement3201DetailVO {

    /** 医疗费用总额 */
    private BigDecimal medFeeSumAmt;
    /** 基金支付总额 */
    private BigDecimal fundPaySumAmt;
    /** 个人账户支付总额 */
    private BigDecimal acctPay;
    /** 个人账户支付总额 */
    private BigDecimal acctGjPay;
    /** 定点医药机构结算笔数 */
    private Integer fixMedInsSetlCnt;

    private String contractNo;

    private Integer insutype;

}
