package com.openhis.web.doctorstation.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 患者详情Dto
 *
 * @author liuhr
 * @date 2025/4/10
 */
@Data
@Accessors(chain = true)
public class PatientDetailsDto {

    /**
     * 就诊id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 患者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /**
     * 患者名字
     */
    private String name;
    /**
     * 性别
     */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /**
     * 生日
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthDate;

    /**
     * 病人年龄
     */
    private String ageString;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 工作单位
     */
    private String workCompany;

    /**
     * 民族
     */
    private String nationalityCode;

    /**
     * 婚姻状态
     */
    private Integer maritalStatusEnum;
    private String maritalStatusEnum_enumText;

    /**
     * 身份证ID
     */
    private String idCard;

    /**
     * 国家编码
     */
    private String countryCode;

    /**
     * 职业
     */
    private Integer prfsEnum;
    private String prfsEnum_enumText;

    /**
     * 联系人
     */
    private String linkName;

    /**
     * 联系人关系
     */
    private Integer linkRelationCode;
    private String linkRelationCode_enumText;

    /**
     * 联系人电话
     */
    private String linkTelcom;

    /** 就诊类别 */
    private Integer classEnum;

    /** 就诊编号 */
    private String busNo;

    /** 挂号医生 */
    private String doctorName;

    /** 账户余额 */
    private BigDecimal balanceAmount;

    /** 账户类型编码 */
    @Dict(dictCode = "")
    private String typeCode;
    private String typeCode_dictText;

    /** 费用总计 */
    private BigDecimal totalPrice;

    /** 费用：药品 */
    private BigDecimal totalMedicationPrice;

    /** 费用：耗材 */
    private BigDecimal totalDevicePrice;

    /** 费用：项目 */
    private BigDecimal totalActivityPrice;

    /** 床位号 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    private Long bedLocationId;
    private String bedLocationId_dictText;

    /** 入院时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 出院时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 挂号时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 入院天数 */
    private Integer admissionDays;

    /** 过敏史 */
    private Integer allergyHistoryFlag;

}