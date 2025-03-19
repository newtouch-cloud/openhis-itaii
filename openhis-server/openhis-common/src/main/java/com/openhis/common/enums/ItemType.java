/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目类型
 *
 * @author zwh
 * @date 2025-03-10
 */
@Getter
@AllArgsConstructor
public enum ItemType implements HisEnumInterface {

    /**
     * 中药
     */
    MEDICINE(1, "1", "中药"),

    /**
     * 耗材
     */
    DEVICE(2, "2", "耗材"),

    /**
     * 医疗活动
     */
    ACTIVITY(3, "3", "医疗活动");

    @EnumValue
    private Integer value;
    private String code;
    private String info;

}
