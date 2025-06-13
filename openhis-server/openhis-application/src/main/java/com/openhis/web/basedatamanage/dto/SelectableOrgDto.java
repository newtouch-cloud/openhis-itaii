/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 可选择切换科室 dto
 */
@Data
@Accessors(chain = true)
public class SelectableOrgDto {

    /** 科室ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;

    /**
     * 科室名称
     */
    private String orgName;

}
