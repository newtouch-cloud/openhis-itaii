/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.annotation.Excel;
import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * [3201]对账记录
 *
 * @author SunJQ
 * @date 2025-04-29
 */
@Data
@Accessors(chain = true)
@TableName("yb_financial_reconcile_record")
@EqualsAndHashCode(callSuper = false)
public class FinancialReconcileRecord extends HisBaseEntity {
    //主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //定点医药机构编号
    @Excel(name = "定点医药机构编号", width = 15)
    @JSONField(serialize=false)
    private String fixmedinsCode;
    //定点医药机构名称
    @Excel(name = "定点医药机构名称", width = 15)
    @JSONField(serialize=false)
    private String fixmedinsName;
    //医保区划
    @Excel(name = "医保区划", width = 15)
    @JSONField(serialize=false)
    private String admvs;
    //险种
    @Excel(name = "险种", width = 15, dictType = "insutype")
    @Dict(dictCode = "insutype")
    @JSONField(name="insutype")
    private String insutype;
    //清算类别
    @Excel(name = "清算类别", width = 15, dictType = "clr_type")
    @Dict(dictCode = "clr_type")
    @JSONField(name="clr_type")
    private String clrType;
    //结算经办机构
    @Excel(name = "结算经办机构", width = 15)
    @JSONField(name="setl_optins")
    private String setlOptins;
    //对账开始日期
    @Excel(name = "对账开始日期", width = 15, dateFormat = "yyyy-MM-dd")
    @JSONField(name="stmt_begndate")
    private String stmtBegndate;
    //对账结束日期
    @Excel(name = "对账结束日期", width = 15, dateFormat = "yyyy-MM-dd")
    @JSONField(name="stmt_enddate")
    private String stmtEnddate;
    //医疗费总额
    @Excel(name = "医疗费总额", width = 15)
    @JSONField(name="medfee_sumamt")
    private Double medfeeSumamt;
    //基金支付总额
    @Excel(name = "基金支付总额", width = 15)
    @JSONField(name="fund_pay_sumamt")
    private Double fundPaySumamt;
    //个人账户支付金额
    @Excel(name = "个人账户支付金额", width = 15)
    @JSONField(name="acct_pay")
    private Double acctPay;
    //定点医药机构结算笔数
    @Excel(name = "定点医药机构结算笔数", width = 15)
    @JSONField(name="fixmedins_setl_cnt")
    private Integer fixmedinsSetlCnt;
    //对账结果
    @Excel(name = "对账结果", width = 15, dictType = "stmt_rslt")
    @Dict(dictCode = "stmt_rslt")
    @JSONField(serialize=false)
    private String stmtRslt;
    //对账结果说明
    @Excel(name = "对账结果说明", width = 15)
    @JSONField(serialize=false)
    private String stmtRsltDscr;

    //医院id
    private Long orgId;

    //入参
    private String param;

    //clr_appy_evt_id 机构清算申请事件ID
    private String result;
}
