package com.openhis.web.reportmanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 出库明细查询条件
 *
 * @author yuanzs
 * @date 2025-04-21
 */
@Data
@Accessors(chain = true)
public class OutboundReportSearchParam {

    /** 单据号 */
    private String supplyBusNo;

    /** 药品名称 */
    private String name;

    /** 编码 */
    private String busNo;

    /** 源仓库(存放库房) */
    private Long sourceLocationId;

    /** 目的仓库(领用科室) */
    private Long purposeLocationId;

    /** 审核日期 */
    private Date approvalTime;

    /** 供应商 */
    private Long supplierId;

}
