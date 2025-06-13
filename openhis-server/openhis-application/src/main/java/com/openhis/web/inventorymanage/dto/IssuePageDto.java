/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 单据分页列表 dto
 *
 * @author CY
 * @date 2025-04-08
 */
@Data
@Accessors(chain = true)
public class IssuePageDto {

    /** 单据号 */
    private String supplyBusNo;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 类型 */
    private Integer typeEnum;
    private String typeEnum_enumText;

    /** 供应商 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_supplier")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplierId;
    private String supplierId_dictText;

    /** 部门名称 */
    private String locationName;

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

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;
    private String purposeTypeEnum_enumText;

}
