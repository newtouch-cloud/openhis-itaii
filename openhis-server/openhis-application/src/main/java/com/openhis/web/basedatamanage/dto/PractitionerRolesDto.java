package com.openhis.web.basedatamanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 参与者角色 dto
 */
@Data
@Accessors(chain = true)
public class PractitionerRolesDto {

    /**
     * 角色id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /**
     * 角色code
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 参与者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;

}
