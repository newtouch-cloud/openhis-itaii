package com.openhis.web.basedatamanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 参与者机构和位置 dto
 */
@Data
@Accessors(chain = true)
public class PractitionerOrgAndLocationDto {

    /**
     * 位置id
     */
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;
    private String locationId_dictText;

    /**
     * 角色code
     */
    private String roleCode;

    /**
     * 机构id
     */
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;
    private String orgId_dictText;

    /**
     * 参与者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;

}
