/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 患者信息
 *
 * @author yuxj
 * @date 2025-04-22
 */
@Data
@Accessors(chain = true)
public class EleInvoicePatientInfoDto {
    // 患者id
    private Long payerId;

    // 患者姓名
    private String payer;

    // 患者院内编码/病历号
    private String caseNumber;

    // 性别
    private Integer genderEnum;
    private String genderEnum_enumText;

    // 生日
    private Date birthDate;

    // 电话
    private String tel;

    // 身份证号
    private String cardNo;

    // 就诊编码
    private String encounterBusNo;

    // 就诊日期
    private String consultationDate;

    // 住院日期
    private String inHospitalDate;

    // 出院日期
    private String outHospitalDate;

    // 就诊科室名称
    private String patientCategory;

    // 就诊科室编码
    private String patientCategoryCode;

    // 医疗机构类型
    private String medicalInstitution;

    // 医保区域编码
    private String medCareAreaCode;

    // 入院科室编码
    private String categoryCode;
    // 入院科室名称
    private String category;
    // 出院科室编码
    private String leaveCategoryCode;
    // 出院科室名称
    private String leaveCategory;

}
