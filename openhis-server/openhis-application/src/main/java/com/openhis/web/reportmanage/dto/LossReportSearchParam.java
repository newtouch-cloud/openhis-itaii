/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 报损明细查询条件
 *
 * @author ym
 * @date 2025-05-21
 */
@Data
@Accessors(chain = true)
public class LossReportSearchParam {

    /** 编码 */
    private String busNo;

    /** 项目名称 */
    private String name;

    /** 报损时间 */
    private Date approvalTime;
}
