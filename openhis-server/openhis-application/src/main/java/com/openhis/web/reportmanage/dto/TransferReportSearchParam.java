/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品调拨明细查询条件
 *
 * @author GYY
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
public class TransferReportSearchParam {

    /** 单据号 */
    private String supplyBusNo;

    /** 药品名称 */
    private String name;

    /** 源仓库 */
    private Long sourceLocationId;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 单据时间 */
    private Date createTime;
}
