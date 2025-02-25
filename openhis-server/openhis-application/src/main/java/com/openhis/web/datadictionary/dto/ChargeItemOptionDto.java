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
    private String value;

    /** 标签 */
    private String label;
}
