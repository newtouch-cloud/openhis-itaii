/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊患者处方 dto
 *
 * @author zwh
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class EncounterPatientPrescriptionDto {

    /** 收费项目类型 */
    private Integer contextEnum;
    private String contextEnum_enumText;

    /** 收费状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 就诊ID */
    private Long encounterId;

    /** 开立科室 */
    private Long requestingOrgId;

    /** 数量 */
    private Long quantityValue;

    /** 单位 */
    private String quantityUnit;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 处方号 */
    private String prescriptionNo;

    /** 业务编码 */
    private String busNo;

    /** 开立人ID */
    private Long entererId;

    /** 开立时间 */
    private Date enteredDate;

    /** 关联账户ID */
    private Long accountId;

    /** 物品编码 */
    private Long itemId;

    /** 物品名称 */
    private String itemName;
}
