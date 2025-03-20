package com.openhis.web.outpatientmanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门诊输液记录Dto
 *
 * @author liuhr
 * @date 2025/3/12
 */
@Data
@Accessors(chain = true)
public class OutpatientInfusionRecordDto {

    /** 服务申请管理表ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serviceId;

    /** 服务申请状态 */
    private Integer requestStatus;

    /** 请求基于什么的ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long basedOnId;

    /** 服务请求编码 */
    private String busNo;

    /** 请求code，输液 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;

    /** 处方号 */
    private String prescriptionNo;

    /** 就诊ID（前台显示用） */
    private String encounterBusNo;

    /** 就诊ID */
    private Long encounterId;

    /** 病人ID（前台显示用） */
    private String patientBusNo;

    /** 病人ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 病人姓名 */
    private String patientName;

    /** 病人性别 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 住院执行次数 */
    private Integer executeNum;

    /** 分组id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    /** 执行护士 */
    @Dict(dictCode = "id", dictTable = "adm_practitioner", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long performerId;
    private String performerId_dictText;

    /** 开单医生 */
    @Dict(dictCode = "id", dictTable = "adm_practitioner", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long doctorId;
    private String doctorId_dictText;

    /** 发放科室 */
    @Dict(dictCode = "id", dictTable = "adm_organization", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;
    private String orgId_dictText;

    /** 预计执行时间 */
    private String occurrenceStartTime;

    /** 预计结束时间 */
    private String occurrenceEndTime;

    /** 药品ID */
    private Long medicationId;

    /** 药品信息 */
    private String medicationInformation;

    /** 药品数量 */
    private BigDecimal medicationQuantity;

    /** 用药频次 */
    private String rateCode;

    /** 单次剂量带剂量单位 */
    private String dose;

    /** 输液速度 */
    private Integer speed;

    /** 药品状态 */
    private Integer medicationStatusEnum;
    private String medicationStatusEnum_enumText;

    /** 皮试标志（是/否） */
    private Integer skinTestFlag;
    private String skinTestFlag_enumText;

    /** 开单时间 */
    private String createTime;

}
