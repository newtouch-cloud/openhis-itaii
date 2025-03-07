package com.openhis.administration.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 岗位管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_practitioner_role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PractitionerRole extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 名称 */
    private String name;

    /** 角色编码 */
    private String role_code;

    /** 活动标识 */
    private Integer activeFlag;

    /** 参与者Id */
    private Long practitionerId;

    /** 机构 */
    private Long orgId;

    /** 位置ID */
    private Long locationId;

    /** 服务id */
    private Long healthcareServiceId;

    /** 专业编码枚举 */
    private Integer specialtyEnum;

    /** 岗位类型 */
    private String typeCode;

    /** 有效时间 */
    private String availabilityJson;

}