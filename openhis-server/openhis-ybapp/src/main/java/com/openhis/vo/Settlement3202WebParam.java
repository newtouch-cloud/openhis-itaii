/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 【3202】前台入参
 *
 * @author SunJQ
 * @date 2025-04-17
 */
@Data
public class Settlement3202WebParam {
    /** 结算经办机构 */
    @NotNull
    private String setlOptins;//前台传入,注意:先上传文件后核对明细的
    /** 清算类别 */
    private String clrType;
    /** 文件查询号 */
    @NotNull
    private String fileQuryNo;
    /** 开始时间 */
    @NotNull
    private String stmtBegnDate;
    /** 结束时间 */
    @NotNull
    private String stmtEndDate;
    /** 医院id */
    private Long orgId;
//    /** 医疗费用总额 */
//    @NotNull
//    private BigDecimal medFeeSumAmt;
//    /** 基金支付总额 */
//    @NotNull
//    private BigDecimal fundPaySumAmt;
//    /** 现金支付金额 */
//    @NotNull
//    private BigDecimal cashPayAmt;
//    /** 定点医药机构结算笔数 */
//    @NotNull
//    private Integer fixMedInsSetlCnt;
}
