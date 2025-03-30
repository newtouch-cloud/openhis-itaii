/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退款项目 dto
 *
 * @author zwh
 * @date 2025-03-18
 */
@Data
@Accessors(chain = true)
public class RefundItemDto {

    /** 收费ID */
    private Long chargeId;

    /** 医疗服务所在表 */
    private String serviceTable;

    /** 医疗服务ID */
    private Long serviceId;

    /** 请求编码 */
    private String busNo;

    /** 请求数量 */
    private Integer quantity;

    /** 请求单位编码 */
    private String unitCode;

    /** 退款状态 */
    private Integer refundStatus;

    /** 项目id */
    private Long itemId;

    /** 发放id */
    private Long dispenseId;

    /** 项目名 */
    private Long itemName;
}
