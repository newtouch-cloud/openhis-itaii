package com.openhis.web.reportmanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 领用退库明细查询条件
 *
 * @author ym
 * @date 2025-05-23
 */
@Data
@Accessors(chain = true)
public class ReturnIssueReportSearchParam {

    /** 单据号 */
    private String supplyBusNo;

    /** 药品名称 */
    private String name;

    /** 编码 */
    private String busNo;

    /** 源仓库(科室) */
    private Long sourceLocationId;

    /** 目的仓库(库房) */
    private Long purposeLocationId;

    /** 审核日期 */
    private Date approvalTime;

    /** 供应商 */
    private Long supplierId;

}
