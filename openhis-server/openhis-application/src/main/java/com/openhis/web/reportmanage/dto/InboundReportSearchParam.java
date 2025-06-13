/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 入库明细查询条件
 *
 * @author GYY
 * @date 2025-04-22
 */
@Data
@Accessors(chain = true)
public class InboundReportSearchParam {

    /** 单据号 */
    private String supplyBusNo;

    /** 药品名称 */
    private String name;

    /** 编码 */
    private String busNo;

    /** 审核时间 */
    private Date approvalTime;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 供应商 */
    private Long supplierId;
}
