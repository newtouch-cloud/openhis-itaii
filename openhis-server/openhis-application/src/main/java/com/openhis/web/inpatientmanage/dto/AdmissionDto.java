package com.openhis.web.inpatientmanage.dto;

import java.util.Date;

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
public class AdmissionDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
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

    /** 类别编码 */
    private Integer classEnum;

     /** 类别医保编码 */
    private Integer ybClassEnum;

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

    /** 病人年龄 */
    private String ageString;

    /** 身份证号 */
    private String idCard;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 入院病区 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    private Long wardLocationId;
    private String wardLocationId_dictText;

    /** 登记员 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
