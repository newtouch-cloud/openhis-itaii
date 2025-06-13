package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门诊划价库存 dto
 */
@Data
@Accessors(chain = true)
public class OutpatientPricingInventoryDto {

    /** 物理表名 */
    private String itemTable;

    /** 实例id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    /** 当前库存数量 ,对应小单位*/
    private BigDecimal quantity;

    /** 单位 , 对应小单位*/
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 产品批号 */
    private String lotNumber;

    /** 库位 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationStoreId;

    /** 库房id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /**
     * 库房名称
     */
    private String locationName;

    /** 采购单价(进价) */
    private BigDecimal price;

}
