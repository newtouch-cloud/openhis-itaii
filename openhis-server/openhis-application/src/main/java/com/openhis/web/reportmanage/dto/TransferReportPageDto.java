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
 * 商品调拨明细报表 dto
 *
 * @author GYY
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
public class TransferReportPageDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String supplyBusNo;

    /** 药品名称 */
    private String name;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 产品批号 */
    private String lotNumber;

    /** 源仓库id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sourceLocationId;

    /** 源仓库名称 */
    private String sourceLocationName;

    /** 源货位id */
    private Long sourceLocationStoreId;

    /** 源货位名称 */
    private String sourceLocationStoreName;

    /** 目的仓库id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long purposeLocationId;

    /** 目的仓库名称 */
    private String purposeLocationName;

    /** 目的货位id */
    private Long purposeLocationStoreId;

    /** 目的货位名称 */
    private String purposeLocationStoreName;

    /** 数量 */
    private BigDecimal itemQuantity;

    /** 计量单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 单价 */
    private BigDecimal price;

    /** 合计金额 */
    private BigDecimal totalPrice;

    /** 申请人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long applicantId;
    private String applicantId_dictText;

    /** 审批人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long approverId;
    private String approverId_dictText;

    /** 制单日期 */
    private Date createTime;

    /** 申请日期 */
    private Date applyTime;

    /** 审批时间(调拨时间) */
    private Date approvalTime;
}
