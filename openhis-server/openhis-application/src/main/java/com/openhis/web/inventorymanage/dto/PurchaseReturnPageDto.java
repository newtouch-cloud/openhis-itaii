/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 入库单据分页列表 dto
 *
 * @author yuanzs
 * @date 2025-04-02
 */
@Data
@Accessors(chain = true)
public class PurchaseReturnPageDto {

    /** 单据号 */
    private String supplyBusNo;

    /** 原始单据号 */
    private String originalSupplyBusNo;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 经手人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;
    private String practitionerId_dictText;

    /** 退货原因 */
    private String reason;

    /** 审核意见 */
    private String remake;

    /** 申请人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicantId;
    private String applicantId_dictText;

    /** 审批人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long approverId;
    private String approverId_dictText;

    /** 期望日期（制单日期） */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name="create_time")
    private Date createTime;

    /** 审批时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name="approval_time")
    private Date approvalTime;

    /** 供应商 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_supplier")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplierId;
    private String supplierId_dictText;

    /** 类型 */
    private Integer typeEnum;
    private String typeEnum_enumText;

}
