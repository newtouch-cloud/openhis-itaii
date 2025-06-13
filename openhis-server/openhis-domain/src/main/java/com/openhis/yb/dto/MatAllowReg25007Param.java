package com.openhis.yb.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【25007】生育津贴登记（输入）
 *
 * @author gaoyy
 * @date 2025-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MatAllowReg25007Param {
    // 1. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 生育类别
    @JSONField(name = "matn_type")
    private String matnType;

    // 3. 计划生育手术或生育日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "birctrl_matn_date")
    private Date birctrlMatnDate;

    // 4. 孕周数
    @JSONField(name = "geso_val")
    private BigDecimal gesoVal;

    // 5. 胎次
    @JSONField(name = "fetts")
    private BigDecimal fetts;

    // 6. 胎儿数
    @JSONField(name = "fetus_cnt")
    private BigDecimal fetusCnt;

    // 7. 经办人姓名
    @JSONField(name = "opter_name")
    private String opterName;

    // 8. 结算ID
    @JSONField(name = "setl_id")
    private String setlId;
}
