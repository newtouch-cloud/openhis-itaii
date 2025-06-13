package com.openhis.web.doctorstation.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 电子处方 dto
 */
@Data
@Accessors(chain = true)
public class ElepPrescriptionInfoParam {

    /** ID */
    private Integer id;

    /** 医院内部处方编号 */
    private String prescriptionNo;

    /** 医院id (前台不传) */
    private Long organizationId;

    /** 门诊/住院病历号 (前台不传) */
    private String iptOtpNo;

    /** 科室病区 (前台不传) */
    private String departmentWard;

    /** 医保类型 (前台不传) */
    private Integer insuranceEnum;

    /** 开具日期 (前台不传) */
    private Date issueTime;

    /** 开具科室 (前台不传) */
    private Long orgId;

    /** 患者 */
    private Long patientId;

    /** 就诊id */
    private Long encounterId;

    /** 诊断id (前台不传) */
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

    /** 开方医师 (前台不传) */
    private Long prescribingDrId;

    /** 延长原因 */
    private String extensionReason;

    /** 处方状态 (前台不传) */
    private Integer statusEnum;

    /** 请求数量 */
    private Integer quantity;

    /** 请求单位 */
    private String unitCode;

    /** 处方类别 */
    private Integer rxTypeCode;
    /** 支持用药信息 */
    private String supportInfo;
    /** 服药时间(开始) */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDoseStart;

    /** 服药时间(结束) */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDoseEnd;
    /** 给药间隔 */
    private String dispenseInterval;

    /** 单次发药数 */
    private Integer dispensePerQuantity;

    /** 每次发药供应天数 */
    private Integer dispensePerDuration;

    /** 药品信息 */
    List<ElepMedicationInfoDto> medicationInfoList;
}
