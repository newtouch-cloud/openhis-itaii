/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 退货单据分页查询条件
 *
 * @author yuanzs
 * @date 2025-04-02
 */
@Data
@Accessors(chain = true)
public class PurchaseReturnSearchParam {

    /** 单据号 */
    @Length(max = 255)
    private String busNo;

    /** 经手人 */
    private Long practitionerId;

    /** 单据时间 */
    private Date occurrenceTime;
}
