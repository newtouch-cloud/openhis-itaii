package com.openhis.web.inpatientmanage.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 住院登记信息
 *
 * @author liuhr
 * @since 2025/04/07
 */
@Data
@Accessors(chain = true)
public class AdmissionUpDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "就诊ID不能为空")
    private Long id;

    /** 患者ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 群组ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    /** 就诊编码 */
    private String encounterBusNo;

    /** 状态编码 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 类别编码 */
    private Integer classEnum;

    /** 类别医保编码 */
    private Integer ybClassEnum;
    private String ybClassEnum_enumText;

    /** 优先级编码 */
    private Integer priorityEnum;
    private String priorityEnum_enumText;

    /** 分类编码 */
    private Integer typeEnum;

    /** 服务ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serviceTypeId;

    /** 就诊对象状态 */
    private Integer subjectStatusEnum;

    /** 开始时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 机构id(科室) */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    private Long organizationId;
    private String organizationId_dictText;

    /** 入院类型 */
    @Dict(dictCode = "admit_source_code")
    private String admitSourceCode;
    private String admitSourceCode_dictText;

    /** 入院方式 */
    @Dict(dictCode = "in_way_code")
    private String inWayCode;
    private String inWayCode_dictText;

    /** 住院次数 */
    private Integer hospitalizationCount;

    /** 患者姓名 */
    private String name;

    /** 患者院内编码/病历号 */
    private String patientBusNo;

    /** 性别编码 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 生日 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthDate;

    /** 婚姻状态 */
    private Integer maritalStatusEnum;
    private String maritalStatusEnum_enumText;

    /** 职业编码 */
    private Integer prfsEnum;
    private String prfsEnum_enumText;

    /** 电话 */
    @Size(min = 11, max = 11, message = "电话长度必须为11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话格式不正确")
    private String phone;

    /** 地址 */
    private String address;

    /** 民族 */
    private String nationalityCode;

    /** 身份证号 */
    @NotBlank(message = "身份证号不能为空")
    @Size(min = 18, max = 18, message = "身份证号必须是18位")
    @Pattern(regexp = "^[0-9Xx]{18}$", message = "身份证号格式不正确")
    private String idCard;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

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

    /** 联系人电话 */
    @Size(min = 11, max = 11, message = "电话长度必须为11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话格式不正确")
    private String linkTelcom;

    /** 病人年龄 */
    private String ageString;

    /** 门诊诊断 */
    private String descriptions;

    /** 接诊医生 */
    private String doctorName;

    /** 入院病区 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    private Long wardLocationId;
    private String wardLocationId_dictText;

    /** 床位数 */
    private String bedCount;

}
