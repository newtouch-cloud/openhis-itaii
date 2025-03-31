/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品调拨分页列表 dto
 *
 * @author zwh
 * @date 2025-02-18
 */
@Data
@Accessors(chain = true)
public class ProductTransferPageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 单据号 */
    private String supplyBusNo;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 类型 */
    private Integer typeEnum;
    private String typeEnum_enumText;

    /** 项目(药品类型) */
    private String itemTable;

    /** 源仓库名称 */
    private String sourceLocationName;

    /** 目的仓库名称 */
    private String purposeLocationName;

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
