/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收费项目详情 dto
 *
 * @author zxy
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class ItemDefinitionDetailDto {

    /** 条件 */
    private Integer conditionCode;
    private String conditionCode_enumText;

    /** 价格 */
    private BigDecimal amount;

}
