/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class PractitionerDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 姓名 */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /** 其他名称 */
    private String nameJson;

    /** 性别编码 */
    // @NotBlank(message = "性别不能为空")
    // private AdministrativeGender genderEnum;
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 生日 */
    private Date birthDate;

    /** 死亡时间 */
    private Date deceasedDate;

    /** 电话 */
    private String phone;

    /** 地址 */
    private String address;

    /** 地址省 */
    private String addressProvince;

    /** 地址市 */
    private String addressCity;

    /** 地址区 */
    private String addressDistrict;

    /** 地址街道 */
    private String addressStreet;

    /** 患者其他地址 */
    private String addressJson;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 患者院内编码/病历号 */
    private String busNo;

    /** 医保码 */
    private String ybNo;

    /** 系统用户id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
}
