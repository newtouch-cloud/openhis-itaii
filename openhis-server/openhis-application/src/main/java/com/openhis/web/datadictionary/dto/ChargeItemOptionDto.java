/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收费项目下拉框
 *
 * @author zxy
 * @date 2025-02-24
 */
@Data
@Accessors(chain = true)
public class ChargeItemOptionDto {

    /** 值 */
    private Integer value;

    /** 标签 */
    private String label;

    public ChargeItemOptionDto(Integer value, String info) {
        this.value = value;
        this.label = info;
    }
}
