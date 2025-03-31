package com.openhis.web.doctorstation.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊患者信息 dto
 */
@Data
@Accessors(chain = true)
public class PatientInfoDto {

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /**
     * 患者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /**
     * 账户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者性别
     */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /**
     * 证件号
     */
    private String idCard;

    /**
     * 电话
     */
    private String phone;

    /**
     * 生日
     */

    private Date birthDate;

    /**
     * 年龄
     */
    private String age;

    /**
     * 就诊状态
     */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /**
     * 过敏史标记
     */
    private Integer allergyHistoryFlag;

    /**
     * 挂号时间
     */
    private Date registerTime;

    /** 账户类型编码 */
    @Dict(dictCode = "account_code")
    private String typeCode;
    private String typeCode_dictText;

}
