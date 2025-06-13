/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.dto;

import com.openhis.administration.domain.ChargeItem;
import com.openhis.financial.domain.Contract;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * chargeItem分组
 *
 * @author SunJQ
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
public class ChargeItemGroupDto {
    /**
     * 合同
     */
    Contract contract;
    /**
     * 收费项
     */
    List<ChargeItem> chargeItemList;
}
