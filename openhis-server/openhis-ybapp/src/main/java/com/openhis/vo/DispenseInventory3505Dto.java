/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 药品发放和库存表连接信息
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class DispenseInventory3505Dto {

    /** 药品发放表主键ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dispenseId;

    /** 就诊id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 药品请求表主键ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long medReqId;

    /** 病人id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 请求单位编码 */
    private String dispenseUnitCode;

    /** 请求数量 */
    private Integer dispenseQuantity;

    /** 发放数量 */
    private Integer dispenseStatus;

    /** 库存项目表主键ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long inventoryId;

    /** 拆零单位 */
    private String inventoryUnitCode;

    /** 当前库存数量(拆零单位) */
    private BigDecimal inventoryQuantity;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 批号 */
    private String lotNumber;

    /** 生产日期 */
    private Date productionDate;

    /** 发药时间 */
    private Date dispenseTime;

    /** 有效期止 */
    private Date expirationDate;

    /** 追溯码 */
    private String traceNo;

    /** 医保编码 */
    private String ybNo;

    /** 就诊编号 */
    private String encounterNo;

    /** 药品编码 */
    private String medicationNo;

    /** 执业资格证号 */
    private String pharPracCertNo;

    /** 发药编号 */
    private String dispenseNo;

    /** 处方标志 */
    private Integer rxFlag;

    /** 配药人 */
    private String preparerName;

    /** 发药人 */
    private String dispenseName;

    /** 开方人 */
    private String practitionerName;
}
