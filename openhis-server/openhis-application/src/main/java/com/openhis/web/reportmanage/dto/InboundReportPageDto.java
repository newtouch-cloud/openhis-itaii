/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 入库明细报表 dto
 *
 * @author GYY
 * @date 2025-04-22
 */
@Data
@Accessors(chain = true)
public class InboundReportPageDto {

    /** 单据号 */
    private String supplyBusno;

    /** 项目名称 */
    private String name;

    /** 编码 */
    private String busNo;

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

    /** 采购数量 */
    private BigDecimal quantity;

    /** 采购单价 */
    private BigDecimal price;

    /** 采购金额 */
    private BigDecimal totalPrice;

    /** 售价 */
    private BigDecimal salePrice;

    /** 售价金额 */
    private BigDecimal totalSalePrice;

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
