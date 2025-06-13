package com.openhis.financial.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import liquibase.pro.packaged.D;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class PaymentRecDetailDto {

    private Long id;

    /** 账户 */
    private Long accountId;

    /** 支付类型 */
    private Integer payEnum;

    /** 支付类型 */
    private String payEnumText;

    /** 金额 */
    private BigDecimal amount;
}
