/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class PractitionerRoleDto {

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
