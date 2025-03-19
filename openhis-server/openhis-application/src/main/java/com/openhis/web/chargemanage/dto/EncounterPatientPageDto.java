/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊患者分页dto
 *
 * @author zwh
 * @date 2025-03-12
 */
@Data
@Accessors(chain = true)
public class EncounterPatientPageDto {

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者院内编码/病历号
     */
    private String patientBusNo;

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
    private String encounterBusNo;

    /**
     * 性别编码
     */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /**
     * 生日
     */
    private Date birthDate;

    /**
     * 账户类型编码
     */
    private String typeCode;

    /**
     * 账户余额
     */
    private BigDecimal balanceAmount;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 年龄
     */
    private String age;

    /** 收费状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;
}
