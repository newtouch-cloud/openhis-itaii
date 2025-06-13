/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存盘点明细查询条件
 *
 * @author GYY
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
public class StocktakingReportSearchParam {

    /** 编码 */
    private String busNo;

    /** 项目名称 */
    private String name;

//    /** 项目类型 */
//    private Integer categoryType;

    /** 盘点时间 */
    private Date approvalTime;
}
