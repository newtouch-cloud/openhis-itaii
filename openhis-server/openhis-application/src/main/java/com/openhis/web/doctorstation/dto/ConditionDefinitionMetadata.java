package com.openhis.web.doctorstation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊断定义 元数据
 */
@Data
@Accessors(chain = true)
public class ConditionDefinitionMetadata {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 所属分类 */
    private Integer sourceEnum;
    private String sourceEnum_enumText;

    /** 编码 */
    private String conditionCode;

    /** 诊断名称 */
    private String name;

    /** 医保标记 */
    private Integer ybFlag;
    private String ybFlag_enumText;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    private Integer ybMatchFlag;
    private String ybMatchFlag_enumText;

    /**
     * 诊断类型(中医诊断或西医诊断)
     */
    private String typeName;

}
