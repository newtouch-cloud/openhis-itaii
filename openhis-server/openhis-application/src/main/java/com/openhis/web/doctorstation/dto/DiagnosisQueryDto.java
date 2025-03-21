package com.openhis.web.doctorstation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊断查询 dto
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class DiagnosisQueryDto {

    /**
     * 诊断ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long conditionId;

    /**
     * 诊断定义id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionId;

    /** 所属分类 */
    private Integer sourceEnum;
    /**
     * 诊断类型(中医诊断或西医诊断)
     */
    private String typeName;

    /** 诊断名称 */
    private String name;

    /**
     * 主诊断标记 (1:是,0:否)
     */
    private Integer maindiseFlag;

    /**
     * 验证状态
     */
    private Integer verificationStatusEnum;
    private String verificationStatusEnum_enumText;

}
