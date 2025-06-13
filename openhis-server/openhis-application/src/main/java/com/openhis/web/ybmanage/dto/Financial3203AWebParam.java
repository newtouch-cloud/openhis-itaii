/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.hssf.record.chart.DatRecord;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 【3203A】前台入参
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
public class Financial3203AWebParam {

    /** 清算类别 */
    @NotNull
    private String clrType;
    /** 开始时间 */
    @NotNull
    private Date stmtBegnDate;
    /** 结束时间 */
    @NotNull
    private Date stmtEndDate;
    /** 清算机构 */
    @NotNull
    private String clrOptins;
}
