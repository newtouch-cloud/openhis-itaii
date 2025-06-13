/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

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
     * 患者
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

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
    @Dict(dictCode = "account_code")
    private String typeCode;
    private String typeCode_dictText;

    /**
     * 账户余额
     */
    private BigDecimal balanceAmount;

    /**
     * 接诊时间
     */
    private Date receptionTime;

    /**
     * 年龄
     */
    private String age;

    /** 收费状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /**
     * 医保总额
     */
    private BigDecimal insuranceAmount;

    /**
     * 自费总额
     */
    private BigDecimal selfAmount;

    /**
     * 付款总额
     */
    private BigDecimal totalAmount;

    /** 合同类型 */
    private Integer categoryEnum;
    private String categoryEnum_enumText;

    /**
     * 结算时间
     */
    private Date billDate;
}
