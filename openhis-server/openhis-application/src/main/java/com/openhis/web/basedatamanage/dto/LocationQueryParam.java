/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.io.Serializable;

import com.openhis.common.enums.LocationMode;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 科室分页查询条件
 *
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class LocationQueryParam implements Serializable {
    /** 模式编码 */
    private LocationMode modeEnum;

}
