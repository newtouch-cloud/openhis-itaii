package com.openhis.web.outpatientmanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门诊皮试记录Dto
 *
 * @author liuhr
 * @date 2025/3/5
 */
@Data
@Accessors(chain = true)
public class OutpatientSkinTestRecordDto {

    /** 服务申请管理表ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 处方号 */
    private String prescriptionNo;

    /** 就诊号 */
    private String encounterBusNo;

    /** 病人ID（前台显示用） */
    private String patientBusNo;

    /** 病人ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 病人姓名 */
    private String patientName;

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

    /** 核对人 */
    @Dict(dictCode = "id", dictTable = "adm_practitioner", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long performerCheckId;
    private String performerCheckId_dictText;

    /** 预计执行时间 */
    private String occurrenceStartTime;

    /** 预计结束时间 */
    private String occurrenceEndTime;

    /** 药品信息 */
    private String medicationInformation;

    /** 药品 */
    private String medicationDetail;

    /** 药品批次号 */
    private String medicationLotNumber;

    /** 药品状态 */
    private Integer medicationStatusEnum;

    /** 皮试结果 */
    private Integer clinicalStatusEnum;
    private String clinicalStatusEnum_enumText;

    /** 皮试检查项目状态 */
    private Integer verificationStatusEnum;;
    private String verificationStatusEnum_enumText;

    /** 备注 */
    private String note;

}
