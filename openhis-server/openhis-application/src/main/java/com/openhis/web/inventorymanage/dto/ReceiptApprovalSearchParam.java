/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 单据审批分页查询条件
 *
 * @author zwh
 * @date 2025-03-19
 */
@Data
@Accessors(chain = true)
public class ReceiptApprovalSearchParam {

    /** 状态 */
    private Integer statusEnum;

    /** 类型 */
    private Integer typeEnum;
}
