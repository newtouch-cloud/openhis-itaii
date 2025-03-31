/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 仓库类型
 *
 * @author zwh
 * @date 2025-03-10
 */
@Getter
@AllArgsConstructor
public enum InventoryType implements HisEnumInterface {

    /**
     * 仓库
     */
    INVENTORY(1, "1", "仓库"),

    /**
     * 药房
     */
    PHARMACY(2, "2", "药房");

    @EnumValue
    private Integer value;
    private String code;
    private String info;

}
