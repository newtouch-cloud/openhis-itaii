package com.openhis.web.doctorstation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊断归属绑定 dto
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class DiagnosisBelongBindingDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 用户ID或科室/机构ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;

    /**
     * 对象名称
     */
    private String objectName;

    /** 诊断定义ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionId;

    /**
     * 诊断(病种)名称
     */
    private String definitionName;

    /** 绑定类型 */
    private Integer bindingEnum;
    private String bindingEnum_enumText;

}
