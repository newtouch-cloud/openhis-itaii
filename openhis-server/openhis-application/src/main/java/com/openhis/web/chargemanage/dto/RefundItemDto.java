/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

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
    @JsonSerialize(using = ToStringSerializer.class)
    private Long chargeItemId;

    /** 收费ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long paymentId;

    /** 医疗服务所在表 */
    private String serviceTable;

    /** 医疗服务ID */
    private Long serviceId;

    /** 请求编码 */
    private String busNo;

    /** 处方号 */
    private String prescriptionNo;

    /** 请求数量 */
    private Integer quantity;

    /** 请求单位编码 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 退款状态 */
    private Integer refundStatus;
    private String refundStatus_enumText;

    /** 发放状态 */
    private Integer dispenseStatus;
    private String dispenseStatus_enumText;

    /** 执行状态 */
    private Integer serviceStatus;
    private String serviceStatus_enumText;

    /** 未发药原因 */
    private Integer notPerformedReason;

    /** 项目id */
    private Long itemId;

    /** 发放id */
    private Long dispenseId;

    /** 请求id */
    private Long requestId;

    /** 项目名 */
    private String itemName;

}
