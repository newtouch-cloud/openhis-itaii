/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class PractitionerRoleDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 名称 */
    @NotBlank(message = "角色名称不能为空")
    private String name;

    /** 角色编码 */
    @NotBlank(message = "角色编码不能为空")
    private String role_code;

    /** 活动标识 */
    @NotNull(message = "活动标识不能为空")
    private Integer activeFlag;

    /** 参与者Id */
    @NotNull(message = "参与者Id不能为空")
    private Long practitionerId;

    /** 机构 */
    @NotNull(message = "机构不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;

    /** 位置ID */
    @NotNull(message = "位置ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /** 服务id */
    @NotNull(message = "服务id不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long healthcareServiceId;

    /** 专业编码枚举 */
    @NotNull(message = "专业编码不能为空")
    private Integer specialtyEnum;

    /** 岗位类型 */
    @NotBlank(message = "岗位类型不能为空")
    private String typeCode;

    /** 有效时间 */
    @NotBlank(message = "有效时间不能为空")
    private String availabilityJson;
}
