/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 出库单据分页查询条件
 *
 * @author CY
 * @date 2025-04-10
 */
@Data
@Accessors(chain = true)
public class IssueSearchParam {

    /** 单据号 */
    private String  supplyBusNo;

    /** 审批状态 */
    private Integer statusEnum;

    /** 经手人 */
    private Long practitionerId;

    /** 单据时间 */
    private Date createTime;

    /** 源仓库 */
    private Long sourceLocationId;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 目的仓库类型 */
    private Integer purposeTypeEnum;

}
