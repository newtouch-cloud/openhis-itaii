/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退费项目id列表 dto
 *
 * @author yangmo
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
public class RefundItemParam {

    /** 付款ID */
    private Long paymentId;

    /** 收费项ID */
    private Long chargeItemId;

    /** 退费标志 */
    private Boolean refundFlg;

}
