/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.outpatientmanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 患者基本信息
 *
 * @author yuxj
 * @date 2025-04-11
 */
@Data
@Accessors(chain = true)
public class OutpatientDisposalPatientInfoDto {

    /** 姓名 */
    private String patientName;

    /** 性别 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 生日 */
    private Date birthDate;

    /** 年龄 */
    private String age;

    /** 证件号 */
    private String idCard;

    /** 就诊科室 */
    private String organizationName;

    /** 就诊日期 */
    private String encounterDate;

    /** 门诊诊断 */
    private String conditionName;

    /** 总金额 */
    private BigDecimal totalPrice;
}
