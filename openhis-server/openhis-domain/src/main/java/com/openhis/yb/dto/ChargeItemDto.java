/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.openhis.administration.domain.ChargeItem;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收费项目
 *
 * @author SunJQ
 * @date 2025-03-31
 */
@Data
@Accessors(chain = true)
public class ChargeItemDto extends ChargeItem {

    private String typeCode;

}
