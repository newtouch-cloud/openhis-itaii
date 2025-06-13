/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 就诊人员列表
 *
 * @author WangYang
 * @date 2025-03-18
 */
@Data
@Accessors(chain = true)
public class EncounterInfoPageDto {

    /** 就诊ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 发放药房 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /** 科室 */
    private String departmentName;

    /** 科室 */
    private Long departmentId;

    /** 患者姓名 */
    private String patientName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 拼音码
     */
    private String patientPyStr;

    /**
     * 五笔码
     */
    private String patientWbStr;

    /**
     * 就诊编码
     */
    private String encounterNo;

    /** 性别 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 发药状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 退药状态 */
    private Integer refundEnum;
    private String refundEnum_enumText;

    /** 就诊日期 */
    private String receptionTime;

    /** 年龄 */
    private String age;

    /** 生日 */
    private Date birthDate;
}
