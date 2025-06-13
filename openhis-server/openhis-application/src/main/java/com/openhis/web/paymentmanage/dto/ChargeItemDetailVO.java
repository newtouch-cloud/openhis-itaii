package com.openhis.web.paymentmanage.dto;

import com.openhis.administration.domain.ChargeItem;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ChargeItemDetailVO {

    @Dict(dictCode = "chrgitm_lv")
    private String dirClass;//医保等级

    private String chargeItemName;//医保等级

    /** 数量 */
    private Integer quantityValue;

    /** 单位 */
    @Dict(dictCode = "prcunt_type")
    private String quantityUnit;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;
}
