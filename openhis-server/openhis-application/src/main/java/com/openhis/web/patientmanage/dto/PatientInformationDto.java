package com.openhis.web.patientmanage.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

/**
 * 病人信息Dto
 *
 * @author liuhr
 * @date 2025/2/22
 */
@Data
public class PatientInformationDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 活动标记 */
    private Integer activeFlag;

    /** 临时标识 */
    private Integer tempFlag;

    /** 患者姓名 */
    @NotBlank(message = "患者姓名不能为空")
    private String name;

    /** 患者其他名称 */
    private String nameJson;

    /** 患者院内编码/病历号 */
    private String busNo;

    /** 性别编码 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 生日 */
    private Date birthDate;

    /** 死亡时间 */
    private String deceasedDate;

    /** 婚姻状态 */
    private Integer maritalStatusEnum;
    private String maritalStatusEnum_enumText;

    /** 职业编码 */
    private Integer prfsEnum;
    private String prfsEnum_enumText;

    /** 电话 */
    @NotNull(message = "电话不能为空")
    @NotBlank(message = "电话不能为空")
    @Size(min = 11, max = 11, message = "电话长度必须为11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话格式不正确")
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

    /** 民族 */
    private String nationalityCode;

    /** 身份证号 */
    @NotNull(message = "身份证号不能为空")
    @Size(min = 18, max = 18, message = "身份证号必须是18位")
    @Pattern(regexp = "^[0-9Xx]{18}$", message = "身份证号格式不正确")
    private String idCard;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 血型ABO */
    private Integer bloodAbo;
    private String bloodAbo_enumText;

    /** 血型RH */
    private Integer bloodRh;
    private String bloodRh_enumText;

    /** 工作单位 */
    private String workCompany;

    /** 籍贯 */
    private String nativePlace;

    /** 国家编码 */
    private String countryCode;

    /** 联系人 */
    private String linkName;

    /** 联系人关系 */
    private Integer linkRelationCode;
    private String linkRelationCode_enumText;

    /** 联系人电话 */
    @Size(min = 11, max = 11, message = "电话长度必须为11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话格式不正确")
    private String linkTelcom;

    /** 其他联系人 */
    private String linkJsons;

    /** 机构Id */
    private Long organizationId;

    /** 机构名 */
    private String organizationName;

    /** 创建时间 */
    private Date createTime;

}
