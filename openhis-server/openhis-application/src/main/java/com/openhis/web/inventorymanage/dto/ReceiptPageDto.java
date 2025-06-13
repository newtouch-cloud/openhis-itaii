/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.hpsf.Decimal;

/**
 * 单据分页列表 dto
 *
 * @author zwh
 * @date 2025-02-18
 */
@Data
@Accessors(chain = true)
public class ReceiptPageDto {

    /** 单据号 */
    private String supplyBusNo;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 退货状态 */
    private Integer returnStatus;

    /** 类型 */
    private Integer typeEnum;
    private String typeEnum_enumText;

    /** 供应商 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_supplier")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplierId;
    private String supplierId_dictText;

    /** 目的仓库 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_location")
    private Long purposeLocationId;
    private String purposeLocationId_dictText;

    /** 目的仓位 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_location")
    private Long purposeLocationStoreId;
    private String purposeLocationStoreId_dictText;

    /** 盈亏金额 */
    private BigDecimal breakevenPrice;

    /** 经手人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;
    private String practitionerId_dictText;

    /** 审批人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long approverId;
    private String approverId_dictText;

    /** 审批时间 */
    private Date approvalTime;

    /** 申请人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    private Long applicantId;
    private String applicantId_dictText;

    /** 申请时间 */
    private Date applyTime;

    /** 制单日期 */
    private Date createTime;

    /** 备注 */
    private String remake;

    /** 项目所在表名 */
    private String itemTable;

    /** 药品类型 */
    private Integer itemType;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;
    private String purposeTypeEnum_enumText;

    /** 追溯码 */
    private String traceNo;

}
