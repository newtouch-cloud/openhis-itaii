/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 组套 dto
 *
 * @author yangmo
 * @date 2025-04-10
 */
@Data
@Accessors(chain = true)
public class OrderGroupDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 名称 */
    private String name;

    /** 类型 */
    private Integer typeEnum;
    private String typeEnum_enumText;

    /** 范围 */
    @Dict(dictCode = "use_range")
    private String rangeCode;
    private String rangeCode_dictText;

    /** 参与者ID */
    private Long practitionerId;

    /** 机构ID */
    private Long orgId;

    /** 组套信息 */
    private String groupJson;

    /** 版本号 */
    private String versionNo;
}
