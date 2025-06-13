package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import liquibase.pro.packaged.D;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Financial3202OtherParam extends Financial3202FileParam {

    // 7. 现金支付金额（数值型，16位含2位小数，必填）
    private BigDecimal cashPayamt;

    private BigDecimal medSumfee;

}
