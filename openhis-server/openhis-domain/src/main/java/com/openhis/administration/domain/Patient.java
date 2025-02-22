package com.openhis.administration.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 患者管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_patient")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Patient extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 活动标记 */
    private Integer activeFlag;

    /** 临时标识 */
    private Integer tempFlag;

    /** 患者姓名 */
    private String name;

    /** 患者其他名称 */
    private String nameJson;

    /** 患者院内编码/病历号 */
    private String busNo;

    /** 性别编码 */
    private Integer genderEnum;

    /** 生日 */
    private Date birthDate;

    /** 死亡时间 */
    private Date deceasedDate;

    /** 婚姻状态 */
    private Integer maritalStatusEnum;

    /** 职业编码 */
    private Integer prfsEnum;

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

    /** 民族 */
    private String nationalityCode;

    /** 身份证号 */
    private String idCard;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 血型ABO */
    private String bloodAbo;

    /** 血型RH */
    private String bloodRh;

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
    private String linkTelcom;

    /** 其他联系人 */
    private String linkJsons;

    /** 机构Id */
    private Long organizationId;


}