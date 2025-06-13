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

/**
 * 报损单分页列表 dto
 *
 * @author gyy
 * @date 2025-04-03
 */
@Data
@Accessors(chain = true)
public class LossReportFormPageDto {

    /** 单据号 */
    private String supplyBusNo;

    /** 单据类型 */
    private Integer type;
    private String type_enumText;

    /** 单据状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 数量 */
    private BigDecimal quantity;

    /** 盘点仓库名称 */
    private String inventoryLocationName;

    /** 报损金额 */
    private BigDecimal reportedLossAmount;

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

    /** 制单日期 */
    private Date createTime;


}
