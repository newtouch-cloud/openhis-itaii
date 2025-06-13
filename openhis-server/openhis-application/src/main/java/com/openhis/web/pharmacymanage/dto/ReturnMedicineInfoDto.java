/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退药信息
 *
 * @author yangmo
 * @date 2025-04-07
 */
@Data
@Accessors(chain = true)
public class ReturnMedicineInfoDto {

    /** 药品请求ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requestId;

    /** 退药ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long refundMedicineId;

    /** 药品发放ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dispenseId;

    /** 药品ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    /** 开单医生 */
    private String doctorName;

    /** 批号 */
    private String lotNumber;

    /** 药品 */
    private String itemName;

    /** 已退数量 */
    private Integer dispenseQuantity;

    /** 单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 退药状态 */
    private Integer refundEnum;
    private String refundEnum_enumText;

    /** 退药请求状态 */
    private Integer reqStatus;
    private String reqStatus_enumText;

    /** 业务表名 */
    private String serviceTable;

    /** 请求数量 */
    private Integer quantity;

}
