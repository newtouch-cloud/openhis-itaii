/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.index.PathBasedRedisIndexDefinition;

import java.util.Date;

/**
 * 【3203】清算申请记录
 *
 * @author SunJQ
 * @date 2025-04-30
 */
@Data
@Accessors(chain = true)
@TableName("yb_financial_apply_record")
@EqualsAndHashCode(callSuper = false)
public class FinancialApplyRecord {
    //主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    //医院id
    private Long orgId;

    //开始时间
    private Date begndate;

    //医院id
    private Date enddate;

    //清算类别
    private Date clrType;

    //医疗费总额
    private Date medfeeSumamt;

    //医保认可费用总额
    private Date medSumfee;

    //基金申报总额
    private Date fundAppySum;

    //现金支付金额
    private Date cashPayamt;

    //个人账户支出
    private Date acctPay;

    //申请人
    private Long praId;

    //入参
    private String param;

    //
    private String outResult;

    //申请状态
    private String status;

}
