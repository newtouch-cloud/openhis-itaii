package com.openhis.web.pharmacymanage.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 明细流水账分页列表 dto
 *
 * @author yuanzs
 * @date 2025-04-14
 */
@Data
@Accessors(chain = true)
public class MedRunningAccountPageDto {

    /** 门诊号 */
    private String outpatientNo;

    /** 住院号 */
    private Integer admissionNo;

    /** 处方号 */
    private String prescriptionNo;

    /** 患者 */
    private String patientName;

    /** 发药人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;
    private String practitionerId_dictText;

    /** 药品项目 */
    private String medicineName;

    /** 规格 */
    private String totalVolume;

    /** 零售价 */
    private BigDecimal retailPrice;

    /** 发药数量 */
    private Integer dispenseQuantity;

    /** 发药金额 */
    private BigDecimal dispensePrice;

    /** 退药数量 */
    private Integer returnQuantity;

    /** 退药金额 */
    private BigDecimal returnPrice;

    /** 批号 */
    private String lotNumber;

    /** 发药时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name="dispense_time")
    private Date dispenseTime;

    /** 厂商 */
    private String manufacturerText;

    /** 供应商 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_supplier")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplyId;
    private String supplyId_dictText;
}
