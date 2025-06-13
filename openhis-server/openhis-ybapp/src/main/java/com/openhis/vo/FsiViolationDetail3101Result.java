package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 【3101】明细审核事前分析服务（输出-违规明细信息）
 * 【3102】明细审核事中分析服务（输出-违规明细信息）
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiViolationDetail3101Result {
    // 1. 违规明细标识
    @JSONField(name = "jrd_id")
    private String jrdId;

    // 2. 参保人标识
    @JSONField(name = "patn_id")
    private String patnId;

    // 3. 就诊标识
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 4. 处方(医嘱)标识
    @JSONField(name = "rx_id")
    private String rxId;

    // 5. 违规明细类型
    @JSONField(name = "vola_item_type")
    private String volaItemType;

    // 6. 违规金额
    @JSONField(name = "vola_amt")
    private BigDecimal volaAmt;
}
