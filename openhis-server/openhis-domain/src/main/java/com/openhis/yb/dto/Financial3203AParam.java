package com.openhis.yb.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.annotation.Excel;
import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *【3203A】清算
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Financial3203AParam extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // 1. 清算类别（字符型，30位，必填）
    @JSONField(name = "clr_type")
    private String clrType;

    // 2. 医疗费总额（数值型，16位含2位小数，必填）
    @JSONField(name = "medfee_sumamt")
    private BigDecimal medfeeSumamt;

    // 3. 医保认可费用总额（数值型，16位含2位小数，必填）
    @JSONField(name = "med_sumfee")
    private BigDecimal medSumfee;

    // 4. 基金申报总额（数值型，16位含2位小数，必填）
    @JSONField(name = "fund_appy_sum")
    private BigDecimal fundAppySum;

    // 5. 现金支付金额（数值型，16位含2位小数，必填）
    @JSONField(name = "cash_payamt")
    private BigDecimal cashPayamt;

    // 6. 个人账户支出（数值型，16位含2位小数，必填）
    @JSONField(name = "acct_pay")
    private BigDecimal acctPay;

    // 7. 开始日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "begndate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begndate;

    // 8. 结束日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "enddate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enddate;

    // 9. 清算机构（字符型，6位，必填）
    @JSONField(name = "clr_optins")
    private String clrOptins; // 必须是准确的6位编码
//
//    // 统筹区号,仅传参用
//    @JSONField(deserialize = false)
//    private String admvs; // 必须是准确的6位编码
}
