/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 供应申请共通查询条件
 *
 * @author my
 * @date 2025-03-19
 */
@Data
@Accessors(chain = true)
public class SupplySearchParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态 */
    private Integer statusEnum;

    /** 源仓库 */
    private Integer sourceLocationId;

    /** 目的仓库 */
    private Integer purposeLocationId;

    /** 申请人 */
    private Long applicantId;

    /** 经手人 */
    private Long practitionerId;
}
