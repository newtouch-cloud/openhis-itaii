package com.openhis.medication.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 药品发放管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("med_medication_dispense")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicationDispense extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 药品发放id */
    private String code;

    /** 药品发放状态 */
    private Integer statusEnum;

    /** 未发药原因 */
    private Integer notPerformedReasonEnum;

    /** 状态变更时间 */
    private Date statusChangedTime;

    /** 发药类型 */
    private Integer dispenseCategory;

    /** 药品编码 */
    private Long medicationId;

    /** 患者id */
    private Long patientId;

    /** 相关诊疗 */
    private Long encounterId;

    /** 支持用药信息 */
    private String supportInfo;

    /** 发药人 */
    private Long practitionerId;

    /** 发放药房 */
    private Long locationId;

    /** 药品请求id */
    private Long medReqId;

    /** 发药类型 */
    private String partTypeCode;

    /** 已发药数量 */
    private Integer dispenseQuantity;

    /** 发药频次 */
    private String dispenseFrequencyCode;

    /** 配药时间 */
    private Date prepareTime;

    /** 发药时间 */
    private Date dispenseTime;

    /** 限制发药时间 */
    private Date limitTime;

    /** 发药目的地 */
    private String desLocationId;

    /** 接收人 */
    private String recPractitionerId;

    /** 用药说明 */
    private String dosageInstruction;

    /** 用法 */
    private String methodCode;

    /** 用药频次 */
    private String frequencyCode;

    /** 单次剂量 */
    private BigDecimal dose;

    /** 剂量单位 */
    private String doseUnitCode;

    /** 单次最大剂量 */
    private BigDecimal maxUnit;


}