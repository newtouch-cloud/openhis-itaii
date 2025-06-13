package com.openhis.web.datadictionary.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊疗子项JSON
 */
@Data
@Accessors(chain = true)
public class ActivityChildJsonDto {

    /** 诊疗医嘱定义ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long adviceDefinitionId;

    /**
     * 子项请求数量
     */
    private Integer childrenRequestNum;

}
