/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.common.dto;

import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 单位列表
 *
 * @author zwh
 * @date 2025-04-01
 */
@Data
@Accessors(chain = true)
public class UnitDto {


    /** 包装单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 最小单位 */
    @Dict(dictCode = "unit_code")
    private String minUnitCode;
    private String minUnitCode_dictText;
}
