/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.domain;

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
    private String fixmedinsCode;
    //定点医药机构名称
    private String fixmedinsName;
    //医保区划
    private String admvs;
    //险种
    private String insutype;
    //清算类别
    private String clrType;
    //结算经办机构
    private String setlOptins;
    //对账开始日期
    private String stmtBegndate;
    //对账结束日期
    private String stmtEnddate;
    //医疗费总额
    private Double medfeeSumamt;
    //基金支付总额
    private Double fundPaySumamt;
    //个人账户支付金额
    private Double acctPay;
    //定点医药机构结算笔数
    private Integer fixmedinsSetlCnt;
    //对账结果
    private String stmtRslt;
    //对账结果说明
    private String stmtRsltDscr;

    //医院id
    private Long orgId;

    //入参
    private String param;

    //
    private String result;
}
