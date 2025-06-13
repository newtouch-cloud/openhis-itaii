/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 领用/退货部门列表 dto
 *
 * @author CY
 * @date 2025-04-03
 */
@Data
@Accessors(chain = true)
public class IssueDepartmentDto {

    /**
     * 部门id
     */
    private String locationId;

    /**
     * 名称
     */
    private String name;

    /**
     * 部门类型枚举
     */
    private String typeEnum;

}
