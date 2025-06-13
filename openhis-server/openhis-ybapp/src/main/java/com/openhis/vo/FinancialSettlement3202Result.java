/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 【3202】医药机构费用结算对明细账-文件输出实体
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FinancialSettlement3202Result {
    // 1. 文件查询号（字符型，30位，必填）
    @JSONField(name = "file_qury_no")
    private String fileQuryNo;

    // 2. 文件名称（字符型，200位，必填）
    @JSONField(name = "filename")
    private String filename;

    // 3. 下载截止时间（日期时间型，必填，格式：yyyy-MM-dd HH:mm:ss）
    @JSONField(name = "dld_endtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dldEndtime;
}
