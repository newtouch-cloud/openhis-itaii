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
 * 药品请求管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("med_medication_request")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicationRequest extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 药品请求编码 */
    private String bus_no;

    /** 处方号 */
    private String prescriptionNo;

    /** 分组id */
    private Long groupId;

    /** 组套id */
    private Long packageId;

    /** 请求数量 */
    private Integer quantity;

    /** 请求单位编码 */
    private String unitCode;

    /** 产品批号 */
    private String lotNumber;

    /** 请求合计（元） */
    private BigDecimal displayAmount;

    /** 药品请求状态 */
    private Integer statusEnum;

    /** 状态原因 */
    private Integer statusReason;

    /** 状态变更时间 */
    private Date statusChangedTime;

    /** 请求意图 */
    private Integer intentEnum;

    /** 请求类型 */
    private Integer categoryEnum;

    /** 优先级 */
    private Integer priorityEnum;

    /** 是否停止执行 */
    private Integer performFlag;

    /** 药品编码 */
    private Long medicationId;

    /** 患者 */
    private Long patientId;

    /** 开方医生 */
    private Long practitionerId;

    /** 发放药房 */
    private Long locationId;

    /** 发放科室 */
    private Long orgId;

    /** 就诊id */
    private Long encounterId;

    /** 支持用药信息 */
    private String supportInfo;

    /** 请求开始时间 */
    private Date reqAuthoredTime;

    /** 是否报告 */
    private Integer reportFlag;

    /** 执行人类型 */
    private Integer performerEnum;

    /** 执行人 */
    private Long performerId;

    /** 执行科室 */
    private Long performOrg;

    /** 设备id */
    private Long deviceDefId;

    /** 记录人 */
    private Long recorderId;

    /** 请求原因 */
    private String reasonJson;

    /** 治疗类型 */
    private Integer therapyEnum;

    /** 服药时间(开始) */
    private Date effectiveDoseStart;

    /** 服药时间(结束) */
    private Date effectiveDoseEnd;

    /** 皮试标志 */
    private Integer skinTestFlag;

    /** 合同Id */
    private String contractNo;

    /** 输液标志 */
    private Integer infusionFlag;

    /** 用法 */
    private String methodCode;

    /** 用药频次 */
    private String rateCode;

    /** 单次剂量 */
    private BigDecimal dose;

    /** 剂量单位 */
    private String doseUnitCode;

    /** 单次最大剂量 */
    private BigDecimal maxDose;

    /** 首次用量 */
    private BigDecimal firstDose;

    /** 首次持续时间 */
    private String firstDuration;

    /** 给药间隔 */
    private String dispenseInterval;

    /** 单次发药数 */
    private Integer dispensePerQuantity;

    /** 每次发药供应天数 */
    private Integer dispensePerDuration;

    /** 发药人 */
    private Long dispenserId;

    /** 输液速度 */
    private Integer speed;
    
}