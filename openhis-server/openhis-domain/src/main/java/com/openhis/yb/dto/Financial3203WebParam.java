/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 【3203】前台入参
 *
 * @author SunJQ
 * @date 2025-04-29
 */
@Data
public class Financial3203WebParam {

    // 清算类别（字符型，30位，必填）
    @JSONField(name = "clr_type")
    private String clrType;

    // 清算方式（字符型，30位，必填）
    @JSONField(name = "clr_way")
    private String clrWay;

    // 开始日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "begndate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begndate;

    // 结束日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "enddate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enddate;

    // 清算年月
    @JSONField(name = "setlym")
    private String setlym;
}
