/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药品类型
 *
 * @author zwh
 * @date 2025-03-10
 */
@Getter
@AllArgsConstructor
public enum CategoryType implements HisEnumInterface {

    /**
     * 中药
     */
    MEDICINE(1, "1", "中药"),

    /**
     * 西药
     */
    CHEMICAL(2, "2", "西药"),

    /**
     * 中成药
     */
    TRADITIONAL(3, "3", "中成药"),

    /**
     * 耗材
     */
    DEVICE(4, "4", "耗材");

    @EnumValue
    private Integer value;
    private String code;
    private String info;

}
