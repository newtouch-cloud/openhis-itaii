package com.openhis.web.pharmacymanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 发药明细账分页列表 dto
 *
 * @author yuanzs
 * @date 2025-04-14
 */
@Data
@Accessors(chain = true)
public class MedDetailedAccountPageDto {

    /** 门诊号 */
    private String outpatientNo;

    /** 处方号 */
    private String prescriptionNo;

    /** 患者 */
    private String patientName;

    /** 发药人 */
    @JsonSerialize(using = ToStringSerializer.class)
    private String practitionerId;

    /** 发药人名称 */
    private String practitionerName;

    /** 药品项目 */
    private String medicationName;

    /** 项目编码 */
    private String busNo;

    /** 发药编码 */
    private String dispenseNo;

    /** 医保编码 */
    private String ybNo;

    /** 规格 */
    private String totalVolume;

    /** 发药数量 */
    private Integer dispenseQuantity;

    /** 发药金额 */
    private BigDecimal dispensePrice;

    /** 退药数量 */
    private Integer refundQuantity;

    /** 退药金额 */
    private BigDecimal refundPrice;

    /** 批号 */
    private String lotNumber;

    /** 厂商 */
    private String manufacturerText;

    /** 供应商 */
    private String supplierName;

    /** 发药单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 退药单位 */
    @Dict(dictCode = "unit_code")
    private String refundUnitCode;
    private String refundUnitCode_dictText;

    /** 发药时间 */
    private Date dispenseTime;

}
