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
 * 医疗参与者管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_practitioner")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Practitioner extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 活动标记 */
    private Integer activeFlag;

    /** 姓名 */
    private String name;

    /** 其他名称 */
    private String nameJson;

    /** 性别编码 */
    private Integer genderEnum;

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
    private String spellCode;

    /** 五笔码 */
    private String wbCode;

    /** 患者院内编码/病历号 */
    private String code;

    /** 医保码 */
    private String ybCode;

    /** 系统用户id */
    private Long userId;


}