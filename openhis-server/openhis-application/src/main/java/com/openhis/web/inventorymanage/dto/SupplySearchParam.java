/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应申请共通查询条件
 *
 * @author my
 * @date 2025-03-19
 */
@Data
@Accessors(chain = true)
public class SupplySearchParam {

    /** 状态 */
    private Integer statusEnum;

    /** 源仓库 */
    private Long sourceLocationId;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 申请人 */
    private Long applicantId;

    /** 经手人 */
    private Long practitionerId;

    /** 单据时间 */
    private Date createTime;

    /** 单据类型 */
    private Integer typeEnum;
}
