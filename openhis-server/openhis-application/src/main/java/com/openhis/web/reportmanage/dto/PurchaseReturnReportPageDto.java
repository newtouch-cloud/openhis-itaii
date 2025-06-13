/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 采购退货明细报表 dto
 *
 * @author ym
 * @date 2025-05-24
 */
@Data
@Accessors(chain = true)
public class PurchaseReturnReportPageDto {

    /** 单据号 */
    private String supplyBusno;

    /** 项目名称 */
    private String name;

    /** 编码 */
    private String busNo;

    /** 采购单据号 */
    private String originalBusNo;

    /** 产品批号 */
    private String lotNumber;

    /** 目的仓库id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long purposeLocationId;

    /** 存放仓库（目的仓库） */
    private String locationName;

    /** 仓库货位 */
    private String locationStoreName;

    /** 计量单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 退货数量 */
    private BigDecimal quantity;

    /** 采购单价 */
    private BigDecimal price;

    /** 退货单价 */
    private BigDecimal returnPrice;

    /** 退货金额 */
    private BigDecimal totalPrice;

    /** 供应商 */
    private String supplier;

    /** 审批人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long approverId;
    private String approverId_dictText;

    /** 制单日期 */
    private Date createTime;

    /** 审批时间 */
    private Date approvalTime;
}
