package com.openhis.financial.model;
import com.openhis.administration.domain.ChargeItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentedItemModel {
    /** 就诊id */
    private Long encounterId;

    /** 层级 */
    private String busNo;

    /** 医疗类型 */
    private String medType;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 总价 */
    private Long chargeItemId;

}
