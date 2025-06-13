package com.openhis.yb.dto;

import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Settlement3201DetailDto {

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
    @Dict(dictCode = "bus_no" , dictTable = "fin_contract" , dictText = "contract_name")
    private String contractNo;
    private String contractNo_dictText;
    @Dict(dictCode = "insutype")
    private Integer insutype;
    private String insutype_dictText;
    @Dict(dictCode = "clr_type")
    private String clrType;
    private String clrType_dictText;

    public Settlement3201DetailDto() {
        this.medFeeSumAmt = BigDecimal.ZERO;
        this.fundPaySumAmt = BigDecimal.ZERO;
        this.acctPay = BigDecimal.ZERO;
        this.acctGjPay = BigDecimal.ZERO;
    }
}
