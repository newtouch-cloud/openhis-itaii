/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 【3101】明细审核事前分析服务（输入-诊断信息）
 * 【3102】明细审核事中分析服务（输入-诊断信息）
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiDiagnose3101Param {

    // 1. 诊断标识
    @JSONField(name = "dise_id")
    private String diseId;

    // 2. 出入诊断类别
    @JSONField(name = "inout_dise_type")
    private String inoutDiseType;

    // 3. 主诊断标志
    @JSONField(name = "maindise_flag")
    private String maindiseFlag;

    // 4. 诊断排序号
    @JSONField(name = "dias_srt_no")
    private String diasSrtNo;

    // 5. 诊断(疾病)编码
    @JSONField(name = "dise_codg")
    private String diseCodg;

    // 6. 诊断(疾病)名称
    @JSONField(name = "dise_name")
    private String diseName;

    // 7. 诊断日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "dise_date")
    private Date diseDate;
}
