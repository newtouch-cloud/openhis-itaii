/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收费项目类型
 *
 * @author zwh
 * @date 2025-03-10
 */
@Getter
@AllArgsConstructor
public enum ChargeItemContext implements HisEnumInterface {

    /**
     * 采购
     */
    PURCHASE(1, "1", "采购"),

    /**
     * 药品
     */
    MEDICATION(2, "2", "药品"),

    /**
     * 耗材
     */
    DEVICE(3, "3", "耗材"),

    /**
     * 项目
     */
    ACTIVITY(4, "4", "项目");

    private final Integer value;
    private final String code;
    private final String info;
}
