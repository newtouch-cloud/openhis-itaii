package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门诊划价价格 dto
 */
@Data
@Accessors(chain = true)
public class OutpatientPricingPriceDto {

    /**
     * 费用定价名称
     */
    private String chargeName;

    /** 费用定价主表ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionId;

    /** 费用定价子表ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionDetailId;

    /** 命中条件 */
    private String conditionCode;

    /** 命中值 */
    private String conditionValue;

    /** 价格 */
    private BigDecimal price;

    /** 单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

}
