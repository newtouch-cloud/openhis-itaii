package com.openhis.administration.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.OrganizationClass;
import com.openhis.common.enums.OrganizationType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 机构管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_organization")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Organization extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编码 */
    private String busNo;

    /** 名称 */
    private String name;

    /** 活动标识 */
    private Integer activeFlag;

    /** 机构类型枚举 */
    private Integer typeEnum;

    /** 机构分类枚举 */
    private Integer classEnum;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 医保码 */
    private String ybNo;

    /** 医保名称 */
    private String ybName;

    /** 科别 */
    private String caty;

    /** 显示顺序 */
    private Integer displayOrder;

    /** 医疗服务机构标识 */
    private String medinsId;

    /** 医疗机构行政区划编码 */
    private String medinsAdmdvs;

    /** 医疗服务机构类型 */
    private String medinsType;

    /** 医疗机构等级 */
    private String medinsLv;

    /** 默认挂号医生 */
    private Long defDoctorId;
}
