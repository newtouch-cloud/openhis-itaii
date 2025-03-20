package com.openhis.web.outpatientmanage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 门诊输液患者显示列表
 *
 * @author liuhr
 * @date 2025/3/13
 */
@Data
@Accessors(chain = true)
public class OutpatientInfusionPatientDto {

    /** 服务申请管理表ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serviceId;

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

    /** 病人性别 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 病人生日 */
    private String birthDate;

    /** 病人身份证号 */
    private String idCard;

    /** 病人年龄 */
    private String ageString;

    /** 开单时间 */
    private String createTime;

}
