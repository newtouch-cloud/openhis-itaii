package com.openhis.workflow.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【请填写功能名称】Entity实体
 *
 * @author system
 * @date 2025-05-07
 */
@Data
@TableName("elep_medication_request")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepMedicationRequest extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /** 医院内部处方编号 */
    private String prescriptionNo;

    /** 医保处方编号 */
    private String hiRxno;

    /** 电子处方追溯码 */
    private String rxTraceCode;

    /** 医院id */
    private Long organizationId;

    /** 门诊/住院病历号 */
    private String iptOtpNo;

    /** 科室病区 */
    private String departmentWard;

    /** 医保类型 */
    private Integer insuranceEnum;

    /** 开具日期 */
    private Date issueTime;

    /** 审核日期 */
    private Date reviewTime;

    /** 撤销日期 */
    private Date quashTime;

    /** 开具科室 */
    private Long orgId;

    /** 患者 */
    private Long patientId;

    /** 就诊id */
    private Long encounterId;

    /** 诊断id */
    private Long conditionId;

    /** 有效天数 */
    private Integer validityDays;

    /** 药品定义id */
    private String medicationId;

    /** 药品剂量 */
    private BigDecimal medDosage;
    /** 药品剂量单位 */
    private String medDosageUnitCode;

    /** 药品频率 */
    private String medFrequency;

    /** 药品途径 */
    private String medRoute;

    /** 开方医师 */
    private Long prescribingDrId;

    /** 审核药师 */
    private Long reviewDrId;

    /** 撤销药师 */
    private Long quashDrId;

    /** 调配药师 */
    private Long dispensingDrId;

    /**  发药药师*/
    private Long issuingDrId;

    /** 延长原因 */
    private String extensionReason;

    /** 处方状态 */
    private Integer statusEnum;

    /** 请求数量 */
    private Integer quantity;

    /** 请求单位 */
    private String unitCode;

    /** 撤销原因 */
    private String quashReason;

    /** 处方类别 */
    private Integer rxTypeCode;
    /** 支持用药信息 */
    private String supportInfo;
    /** 服药时间(开始) */
    private Date effectiveDoseStart;

    /** 服药时间(结束) */
    private Date effectiveDoseEnd;
    /** 给药间隔 */
    private String dispenseInterval;

    /** 单次发药数 */
    private Integer dispensePerQuantity;

    /** 每次发药供应天数 */
    private Integer dispensePerDuration;
}