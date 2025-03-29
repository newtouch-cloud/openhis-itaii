/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 患者基本信息
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class PrescriptionPatientInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 姓名 */
    private String patientName;

    /** 性别 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 生日 */
    private Date birth_date;

    /** 年龄 */
    private String age;

    /** 合同类型 */
    private Integer categoryEnum;
    private String categoryEnum_enumText;

    /** 证件号 */
    private String idCard;

    /** 就诊科室 */
    private String organizationName;

    /** 就诊日期 */
    private String encounterDate;

    /** 总金额 */
    private BigDecimal totalPrice;
}